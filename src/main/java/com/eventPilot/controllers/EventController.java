package com.eventPilot.controllers;

import com.eventPilot.models.*;
import com.eventPilot.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/rest/api")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/createBusySchedule")
    public ResponseEntity<Event> createBusySchedule (@RequestBody Event event) {
        return eventService.createBusySchedule(event);
    }

    @GetMapping("/getEvent")
    public ResponseEntity<List<Event>> getEventsByUser (@RequestBody UserDateInfo userDateInfo) {
        return eventService.getEventsByUser(userDateInfo);
    }

    @GetMapping("/getConflictEvent")
    public ResponseEntity<List<Event>> getConflictEventsByUser (@RequestBody UserDateInfo userDateInfo) {
        return eventService.getConflictEventsByUser(userDateInfo);
    }

    @GetMapping("/getUpcomingEmptySlot")
    public ResponseEntity<List<TimeSlot>> getUpcomingEmptySlot (@RequestBody SchedulingRequest schedulingRequest) {
        return eventService.getUpcomingEmptySlot(schedulingRequest);
    }

    @PostMapping("/createEventWithOtherUsers")
    public ResponseEntity<Event> createEventWithOtherUsers (@RequestBody Event event) {
        return eventService.createEventWithOtherUsers(event);
    }
}