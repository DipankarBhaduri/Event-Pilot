package com.eventPilot.controllers;

import com.eventPilot.models.*;
import com.eventPilot.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controller class for handling event-related RESTful API endpoints.
 * Author: [Dipankar Bhaduri]
 */
@RestController
@RequestMapping("/rest/api")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * Create a busy schedule event.
     * @param event The event object representing a busy schedule.
     * @return ResponseEntity<Event> - Created event details with HTTP status.
     */
    @PostMapping("/createBusySchedule")
    public ResponseEntity<Event> createBusySchedule (@RequestBody Event event) {
        return eventService.createBusySchedule(event);
    }

    /**
     * Retrieve events for a user based on user and date information.
     * @param userDateInfo The object containing user and date information.
     * @return ResponseEntity<List<Event>> - List of events with HTTP status.
     */
    @GetMapping("/getEvent")
    public ResponseEntity<List<Event>> getEventsByUser (@RequestBody UserDateInfo userDateInfo) {
        return eventService.getEventsByUser(userDateInfo);
    }

    /**
     * Retrieve conflicting events for a user based on user and date information.
     * @param userDateInfo The object containing user and date information.
     * @return ResponseEntity<List<Event>> - List of conflicting events with HTTP status.
     */
    @GetMapping("/getConflictEvent")
    public ResponseEntity<List<Event>> getConflictEventsByUser (@RequestBody UserDateInfo userDateInfo) {
        return eventService.getConflictEventsByUser(userDateInfo);
    }

    /**
     * Retrieve upcoming empty time slots based on scheduling request.
     * @param schedulingRequest The object containing scheduling request information.
     * @return ResponseEntity<List<TimeSlot>> - List of upcoming empty time slots with HTTP status.
     */
    @GetMapping("/getUpcomingEmptySlot")
    public ResponseEntity<List<TimeSlot>> getUpcomingEmptySlot (@RequestBody SchedulingRequest schedulingRequest) {
        return eventService.getUpcomingEmptySlot(schedulingRequest);
    }

    /**
     * Create an event involving multiple users.
     * @param event The event object representing an event involving other users.
     * @return ResponseEntity<Event> - Created event details with HTTP status.
     */
    @PostMapping("/createEventWithOtherUsers")
    public ResponseEntity<Event> createEventWithOtherUsers (@RequestBody Event event) {
        return eventService.createEventWithOtherUsers(event);
    }
}