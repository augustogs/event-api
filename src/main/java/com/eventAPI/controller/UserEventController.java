package com.eventAPI.controller;

import com.eventAPI.dto.*;
import com.eventAPI.exception.ErrorResponse;
import com.eventAPI.service.UserEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/events")
public class UserEventController {

    private final UserEventService userEventService;

    @Autowired
    public UserEventController(UserEventService userEventService) {
        this.userEventService = userEventService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUserToEvent(@RequestBody UserEventDTO userEventDTO) {
        try {
            UserEventDTO userRegistered = userEventService.registerUserToEvent(userEventDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userRegistered);
        } catch (Exception e) {
            ErrorResponse err = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeUserFromEvent(@RequestBody UserEventDTO userEventDTO) {
        try {
            String response = userEventService.removeUserFromEvent(userEventDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ErrorResponse err = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> listUserRegistrations(@PathVariable Long userId) {
        try {
            List<EventDTO> events = userEventService.listUserRegistrations(userId);
            return ResponseEntity.status(HttpStatus.OK).body(events);
        } catch (Exception e) {
            ErrorResponse err = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> listEventRegistrations(@PathVariable Long eventId) {
        try {
            List<UserDTO> users = userEventService.listEventRegistrations(eventId);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception e) {
            ErrorResponse err = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
        }
    }

    @PostMapping("/presence")
    public ResponseEntity<?> registerUserPresence(@RequestBody UserPresenceDTO userPresenceDTO) {
        try {
            String response = userEventService.registerUserPresence(userPresenceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ErrorResponse err = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }


    @PostMapping("/reserve")
    public ResponseEntity<?> registerReserve(@RequestBody UserReserveDTO userReserveDTO) {
        try {
            String response = userEventService.registerReserve(userReserveDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ErrorResponse err = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

    @PostMapping("/convert")
    public ResponseEntity<?> convertReserveToRegistration(@RequestBody ReserveIdDTO reserveIdDTO) {
        try {
            String response = userEventService.convertReserveToRegistration(reserveIdDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ErrorResponse err = new ErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }
}
