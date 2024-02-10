package com.eventPilot.services;

import com.eventPilot.models.*;
import com.eventPilot.repository.GenericsCollectionHandler;
import com.eventPilot.utility.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for handling event-related operations.
 * This class interacts with CommonUtils and GenericsCollectionHandler to perform CRUD operations on Event entities.
 * Author: [Dipankar Bhaduri]
 */
@Service
public class EventService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private GenericsCollectionHandler genericsCollectionHandler;

    /**
     * Create a busy schedule event and update the user's schedule.
     * @param event The event object representing a busy schedule.
     * @return ResponseEntity<Event> - Created event details with HTTP status.
     */
    public ResponseEntity<Event> createBusySchedule(Event event) {
        Event eventObject = null;
        try {
            Optional<List<User>> optionalUserList = Optional.ofNullable(genericsCollectionHandler.findByField(User.class, "_id", event.getOrganizerReference().get_id()));

            if (optionalUserList.isPresent() && !optionalUserList.get().isEmpty()) {
                User currentUser = optionalUserList.get().get(0);

                if (currentUser.getEventsByDate() == null) currentUser.setEventsByDate(new HashMap<>());
                Map<String, List<Reference>> usersBusySchedule = currentUser.getEventsByDate();
                DateTimeComponent dateTimeComponent = commonUtils.separateDateAndTime(event.getStartTime());
                String date = dateTimeComponent.getDate().toString();

                if (!usersBusySchedule.containsKey(date)) {
                    usersBusySchedule.put(date, Arrays.asList(commonUtils.getReferenceFromObject(event.get_id(), event.getTitle())));
                } else {
                    usersBusySchedule.get(date).add(commonUtils.getReferenceFromObject(event.get_id(), event.getTitle()));
                }

                eventObject = genericsCollectionHandler.insertData(event);
                genericsCollectionHandler.insertData(currentUser);
            }
            return new ResponseEntity<>(eventObject, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(eventObject, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Retrieve events for a user based on user and date information.
     * @param userDateInfo The object containing user and date information.
     * @return ResponseEntity<List<Event>> - List of events with HTTP status.
     */
    public ResponseEntity<List<Event>> getEventsByUser(UserDateInfo userDateInfo) {
        try {
            Optional<LocalDate> optionalLocalDate = Optional.ofNullable(userDateInfo.getLocalDate());
            Optional<Reference> optionalUserReference = Optional.ofNullable(userDateInfo.getUserReference());

            if (optionalLocalDate.isPresent() && optionalUserReference.isPresent()) {
                LocalDate localDate = optionalLocalDate.get();
                Reference userReference = optionalUserReference.get();
                Optional<List<User>> optionalUserList = Optional.ofNullable(genericsCollectionHandler.findByField(User.class, "_id", userReference.get_id()));

                if (optionalUserList.isPresent() && !optionalUserList.get().isEmpty()) {
                    User currentUser = optionalUserList.get().get(0);

                    if (currentUser.getEventsByDate() == null || currentUser.getEventsByDate().get(localDate.toString()) == null) {
                        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
                    }

                    List<String> eventIds = commonUtils.getIdsFromReferences(currentUser.getEventsByDate().get(localDate.toString()));
                    Optional<List<Event>> eventList = Optional.ofNullable(genericsCollectionHandler.findByFields(Event.class, "_id", eventIds));
                    return new ResponseEntity<>(eventList.get(), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Get conflicting events for a user based on date information.
     *
     * @param userDateInfo The object containing user and date information.
     * @return ResponseEntity<List<Event>> - List of conflicting events with HTTP status.
     */
    public ResponseEntity<List<Event>> getConflictEventsByUser(UserDateInfo userDateInfo) {
        List<Event> eventList = null;
        try {
            ResponseEntity<List<Event>> eventsByUser = getEventsByUser(userDateInfo);

            if (eventsByUser.getBody().size() > 0) {
                String[] startTimes = new String[eventsByUser.getBody().size()];
                String[] endTimes = new String[eventsByUser.getBody().size()];
                String[] eventIds = new String[eventsByUser.getBody().size()];

                PopulateTimeRanges(eventsByUser.getBody(), startTimes, endTimes, eventIds);
                performShortingBasedOnStartTime(startTimes, endTimes, eventIds);
                List<String> conflictEventIds = getConflictEventIds(startTimes, endTimes, eventIds);
                eventList = eventsByUser.getBody().stream().filter(event -> conflictEventIds.contains(event.get_id())).toList();
            }

            return new ResponseEntity<>(eventList, HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(eventList, HttpStatus.OK);
        }
    }

    /**
     * Populate arrays with start times, end times, and event IDs from the list of events.
     *
     * @param eventList   List of events to extract time ranges and event IDs from.
     * @param startTimes  Array to store start times.
     * @param endTimes    Array to store end times.
     * @param eventIds    Array to store event IDs.
     */
    private void PopulateTimeRanges(List<Event> eventList, String []startTimes, String []endTimes, String []eventIds) {
        AtomicInteger index = new AtomicInteger(0);

        if (eventList != null && startTimes != null && endTimes != null && eventIds != null) {
            eventList.stream().forEach(event -> {
                String start = commonUtils.separateDateAndTime(event.getStartTime()).getTime().toString();
                String end = commonUtils.separateDateAndTime(event.getEndTime()).getTime().toString();
                String _id = event.get_id();

                startTimes[index.get()] = start;
                endTimes[index.get()] = end;
                eventIds[index.get()] = _id;

                index.set(index.get() + 1);
            });
        }
    }

    /**
     * Perform sorting of arrays based on start times using bubble sort algorithm.
     *
     * @param startTimes Array of start times.
     * @param endTimes   Array of end times.
     * @param eventIds   Array of event IDs.
     */
    private void performShortingBasedOnStartTime(String[] startTimes, String[] endTimes, String[] eventIds) {
        try {
            int n = startTimes.length;
            boolean swapped;

            for (int i = 0; i < n - 1; i++) {
                swapped = false;

                for (int j = 0; j < n - i - 1; j++) {
                    if (startTimes[j].compareTo(startTimes[j + 1]) > 0) {
                        // For Start Time
                        String tempForStartTime = startTimes[j];
                        startTimes[j] = startTimes[j + 1];
                        startTimes[j + 1] = tempForStartTime;

                        // For End Time
                        String tempForEndTime = endTimes[j];
                        endTimes[j] = endTimes[j + 1];
                        endTimes[j + 1] = tempForEndTime;

                        // For End Time
                        String tempForEventId = eventIds[j];
                        eventIds[j] = eventIds[j + 1];
                        eventIds[j + 1] = tempForEventId;

                        swapped = true;
                    }
                }

                if (!swapped) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get conflicting event IDs based on start times and end times.
     *
     * @param startTimes Array of start times.
     * @param endTimes   Array of end times.
     * @param eventIds   Array of event IDs.
     * @return List<String> - List of conflicting event IDs.
     */
    private List<String> getConflictEventIds(String[] startTimes, String[] endTimes, String[] eventIds) {
        HashSet<String> ids = new HashSet<>();

        try {
            int i = 1, j = 0;
            int n = startTimes.length;

            while (i < n && j < n) {

                if (i == j) {
                    i++;
                } else if (startTimes[i].compareTo(endTimes[j]) <= 0) {
                    ids.add(eventIds[i]);
                    ids.add(eventIds[j]);
                    i++;
                } else if (startTimes[i].compareTo(endTimes[j]) > 0) {
                    j++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(ids);
    }

    /**
     * Get upcoming empty time slots based on scheduling request.
     * @param schedulingRequest The object containing scheduling request information.
     * @return ResponseEntity<List<TimeSlot>> - List of upcoming empty time slots with HTTP status.
     */
    public ResponseEntity<List<TimeSlot>> getUpcomingEmptySlot(SchedulingRequest schedulingRequest) {
        List<TimeSlot> freeTimeSlots = null;

        try {
            Optional<List<Reference>> userReferenceList = Optional.ofNullable(schedulingRequest.getUsers());

            if (userReferenceList.isPresent() && !userReferenceList.get().isEmpty() && schedulingRequest.getDuration() > 0 && schedulingRequest.getLocalDate() != null) {
                List<String> userIds = userReferenceList.get().stream().map(Reference::get_id).toList();
                Optional<List<User>> userList = Optional.ofNullable(genericsCollectionHandler.findByFields(User.class, "_id", userIds));

                if (userList.isPresent() && !userList.get().isEmpty()) {
                    String localDate = schedulingRequest.getLocalDate().toString();
                    List<String> eventIds = getEventIdsFromUserList(userList, localDate);

                    if (eventIds != null) {
                        Optional<List<Event>> eventList = Optional.ofNullable(genericsCollectionHandler.findByFields(Event.class, "_id", eventIds));

                        if (eventList.isPresent()) {
                            List<Meeting> meetingList = eventList.get().stream()
                                    .map(event -> new Meeting(event.getStartTime(), event.getEndTime()))
                                    .collect(Collectors.toList());

                            freeTimeSlots = calculateFreeTime(meetingList, schedulingRequest.getDuration());
                            return new ResponseEntity<>(freeTimeSlots, HttpStatus.OK);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(freeTimeSlots, HttpStatus.BAD_REQUEST);
    }

    /**
     * Helper method to retrieve event IDs from a list of users for a specific date.
     *
     * @param userList   List of users to retrieve event IDs from.
     * @param localDate   The local date for which events are to be retrieved.
     * @return List<String> - List of event IDs.
     */
    private static List<String> getEventIdsFromUserList(Optional<List<User>> userList, String localDate) {
        return userList.get().stream()
                .filter(user -> user.getEventsByDate() != null && user.getEventsByDate().containsKey(localDate))
                .flatMap(user -> user.getEventsByDate().getOrDefault(localDate, Collections.emptyList()).stream())
                .map(Reference::get_id)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to calculate free time slots based on a list of meetings and required duration.
     *
     * @param meetings                List of meetings to calculate free time from.
     * @param requiredDurationInHours Required duration for free time slots in hours.
     * @return List<TimeSlot> - List of calculated free time slots.
     */
    private static List<TimeSlot> calculateFreeTime(List<Meeting> meetings, int requiredDurationInHours) {
        Duration requiredDuration = Duration.ofHours(requiredDurationInHours);
        List<TimeSlot> freeTimeSlots = new ArrayList<>();

        try {
            meetings.sort(Comparator.comparing(Meeting::getStart));
            LocalDateTime previousEnd = LocalDateTime.parse("2024-02-10T00:00:00");

            for (Meeting meetup : meetings) {
                LocalDateTime currentStart = meetup.getStart();

                if (currentStart.isAfter(previousEnd)) {
                    Duration gapDuration = Duration.between(previousEnd, currentStart);

                    if (gapDuration.compareTo(requiredDuration) >= 0) {
                        freeTimeSlots.add(new TimeSlot(previousEnd, previousEnd.plus(gapDuration)));
                    }
                }
                previousEnd = meetup.getEnd();
            }
            LocalDateTime endOfDay = LocalDateTime.parse("2024-02-10T23:59:59");

            if (endOfDay.isAfter(previousEnd)) {
                Duration remainingDuration = Duration.between(previousEnd, endOfDay);

                if (remainingDuration.compareTo(requiredDuration) >= 0) {
                    freeTimeSlots.add(new TimeSlot(previousEnd, previousEnd.plus(remainingDuration)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return freeTimeSlots;
    }

    /**
     * Create an event involving multiple users.
     * @param event The event object representing an event involving other users.
     * @return ResponseEntity<Event> - Created event details with HTTP status.
     */
    public ResponseEntity<Event> createEventWithOtherUsers(Event event) {
        Event eventObject = null;

        try {
            List<String> userIds = Stream.concat(Stream.of(event.getOrganizerReference().get_id()),
                    event.getParticipants().stream().map(Reference::get_id)).collect(Collectors.toList());

            Optional<List<User>> optionalUserList = Optional.ofNullable(genericsCollectionHandler.findByFields(User.class, "_id", userIds));

            if (optionalUserList.isPresent() && !optionalUserList.get().isEmpty()) {
                optionalUserList.get().stream().forEach(currentUser -> {

                    if (currentUser.getEventsByDate() == null) currentUser.setEventsByDate(new HashMap<>());
                    Map<String, List<Reference>> usersBusySchedule = currentUser.getEventsByDate();
                    DateTimeComponent dateTimeComponent = commonUtils.separateDateAndTime(event.getStartTime());
                    String date = dateTimeComponent.getDate().toString();

                    if (!usersBusySchedule.containsKey(date)) {
                        usersBusySchedule.put(date, Arrays.asList(commonUtils.getReferenceFromObject(event.get_id(), event.getTitle())));
                    } else {
                        usersBusySchedule.get(date).add(commonUtils.getReferenceFromObject(event.get_id(), event.getTitle()));
                    }
                    genericsCollectionHandler.insertData(currentUser);
                });
                eventObject = genericsCollectionHandler.insertData(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(eventObject, HttpStatus.OK);
    }
}