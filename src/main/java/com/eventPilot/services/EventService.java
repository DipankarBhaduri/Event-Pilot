package com.eventPilot.services;

import com.eventPilot.models.*;
import com.eventPilot.repository.GenericsCollectionHandler;
import com.eventPilot.utility.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private CommonUtils commonUtils;
    @Autowired
    private GenericsCollectionHandler genericsCollectionHandler;

    public ResponseEntity<Event> createBusySchedule(Event event) {
        Event eventObject = null;
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
    }

    public ResponseEntity<List<Event>> getEventsByUser(UserDateInfo userDateInfo) {
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

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Event>> getConflictEventsByUser(UserDateInfo userDateInfo) {
        ResponseEntity<List<Event>> eventsByUser = getEventsByUser(userDateInfo);

        if (eventsByUser.getBody().size() > 0) {
            String []startTimes = new String[eventsByUser.getBody().size()];
            String []endTimes = new String[eventsByUser.getBody().size()];
            String []eventIds = new String[eventsByUser.getBody().size()];

            PopulateTimeRanges(eventsByUser.getBody(), startTimes, endTimes, eventIds);
            performShortingBasedOnStartTime(startTimes, endTimes, eventIds);
            List<String> conflictEventIds = getConflictEventIds(startTimes, endTimes, eventIds);

            List<Event> eventList = eventsByUser.getBody().stream().filter(event -> conflictEventIds.contains(event.get_id())).toList();
            return new ResponseEntity<>(eventList, HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    private void PopulateTimeRanges(List<Event> eventList, String []startTimes, String []endTimes, String []eventIds) {
        AtomicInteger index = new AtomicInteger(0);

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

    private void performShortingBasedOnStartTime(String[] startTimes, String[] endTimes, String[] eventIds) {
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
    }

    private List<String> getConflictEventIds(String[] startTimes, String[] endTimes, String[] eventIds) {

        HashSet<String> ids = new HashSet<>();
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

        return new ArrayList<>(ids);
    }

    public ResponseEntity<List<ResponseEmptySlots>> getUpcomingEmptySlot(SchedulingRequest schedulingRequest) {
        Optional<List<Reference>> userReferenceList = Optional.ofNullable(schedulingRequest.getUsers());

        if (userReferenceList.isPresent() && !userReferenceList.get().isEmpty() && schedulingRequest.getDuration() > 0 && schedulingRequest.getLocalDate() != null) {
            List<String> userIds = userReferenceList.get().stream().map(Reference::get_id).toList();
            Optional<List<User>> userList = Optional.ofNullable(genericsCollectionHandler.findByFields(User.class, "_id", userIds));

            if (userList.isPresent() && !userList.get().isEmpty()) {
                String localDate = schedulingRequest.getLocalDate().toString();
                List<String> eventIds = userList.get().stream()
                        .filter(user -> user.getEventsByDate() != null && user.getEventsByDate().containsKey(localDate))
                        .flatMap(user -> user.getEventsByDate().getOrDefault(localDate, Collections.emptyList()).stream())
                        .map(Reference::get_id)
                        .collect(Collectors.toList());

                if (eventIds != null) {
                    Optional<List<Event>> eventList = Optional.ofNullable(genericsCollectionHandler.findByFields(Event.class, "_id", eventIds));

                }
            }
        }
    }
}