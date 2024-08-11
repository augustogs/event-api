package com.eventAPI.controller;

import com.eventAPI.dto.EventDTO;
import com.eventAPI.exception.ErrorResponse;
import com.eventAPI.exception.EventAlreadyRegisteredExecption;
import com.eventAPI.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventDTO eventDTO) {
        try {
            EventDTO createdEvent = eventService.createEvent(eventDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } catch (EventAlreadyRegisteredExecption ex) {
            ErrorResponse err = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }
}