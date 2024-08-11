package com.eventAPI.service;

import com.eventAPI.dto.*;
import com.eventAPI.exception.*;
import com.eventAPI.model.Event;
import com.eventAPI.model.Reservation;
import com.eventAPI.model.User;
import com.eventAPI.repository.EventRepository;
import com.eventAPI.repository.ReservationRepository;
import com.eventAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserEventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public UserEventService(UserRepository userRepository, EventRepository eventRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.reservationRepository = reservationRepository;
    }

    public UserEventDTO registerUserToEvent(UserEventDTO userEventDTO) {
        User user = findUserByEmail(userEventDTO.getUserEmail());
        Event event = findEventById(userEventDTO.getEventId());

        checkIfUserAlreadyRegistered(user, event);
        checkIfEventIsFullyBooked(event);
        checkIfEventHasStarted(event);

        event.addUser(user);
        eventRepository.save(event);

        return new UserEventDTO(user.getEmail(), event.getId());
    }

    public String removeUserFromEvent(UserEventDTO userEventDTO) {
        User user = findUserByEmail(userEventDTO.getUserEmail());
        Event event = findEventById(userEventDTO.getEventId());

        checkIfUserIsRegisteredForEvent(user, event);
        checkIfUserHasCheckedIn(user, event);

        removeUser(user, event);
        return "User removed successfully!";
    }

    public List<EventDTO> listUserRegistrations(Long userId) {
        User user = findUserById(userId);
        if (user.getEvents().isEmpty()) {
            throw new UserNotRegisteredForAnyEventException("User is not registered for any Event!");
        }
        return user.getEvents().stream()
                .map(event -> new EventDTO(event.getId(), event.getName(), event.getVacancies(), event.getStartDate(), event.getEndDate()))
                .collect(Collectors.toList());
    }

    public List<UserDTO> listEventRegistrations(Long eventId) {
        Event event = findEventById(eventId);
        if (event.getUsers().isEmpty()) {
            throw new EventHasNoRegisteredUsersException("Event has no registered users!");
        }

        return event.getUsers().stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public String registerUserPresence(UserPresenceDTO userPresenceDTO) {
        User user = findUserByEmail(userPresenceDTO.getUserEmail());
        Event event = findEventById(userPresenceDTO.getEventId());

        checkIfUserIsRegisteredForEvent(user, event);
        checkIfUserAlreadyRegisteredPresence(event, user);
        checkIfWithinAllowedTimeframe(event);

        event.registerPresence(user.getId());
        eventRepository.save(event);
        return "User presence registered successfully!";
    }

    public String registerReserve(UserReserveDTO userReserveDTO) {
        User user = findUserByEmail(userReserveDTO.getUserEmail());
        Event event = findEventById(userReserveDTO.getEventId());

        checkIfEventAlreadyStarted(event);
        checkIfEventIsFullyBooked(event);
        checkIfUserAlreadyRegisteredForEvent(user, event);
        checkIfUserAlreadyHasReservation(user, event);

        Reservation reservation = new Reservation(user, event);
        reservationRepository.save(reservation);
        return "Reserve registered successfully!";
    }

    public String convertReserveToRegistration(ReserveIdDTO reserveIdDTO) {
        Reservation reservation = findReservationById(reserveIdDTO.getReserveId());
        UserEventDTO userEventDTO = new UserEventDTO(reservation.getUser().getEmail(), reservation.getEvent().getId());

        registerUserToEvent(userEventDTO);
        reservationRepository.delete(reservation);
        return "Reservation successfully converted to registration!";
    }

    // Private methods
    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    private Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found!"));
    }

    private void checkIfUserAlreadyRegistered(User user, Event event) {
        if (event.getUsers().contains(user)) {
            throw new UserAlreadyRegisteredException("User already registered for this Event!");
        }
    }

    private void checkIfEventIsFullyBooked(Event event) {
        if (event.getUsers().size() >= event.getVacancies()) {
            throw new EventFullyBookException("Event is fully booked!");
        }
    }

    private void checkIfEventHasStarted(Event event) {
        LocalDateTime timeNow = LocalDateTime.now();
        if (timeNow.isAfter(event.getStartDate())) {
            throw new EventAlreadyStartedException("Event is already started!");
        }
    }

    private void checkIfUserIsRegisteredForEvent(User user, Event event) {
        if (!event.getUsers().contains(user)) {
            throw new UserNotRegisteredForEventException("User is not registered for the event!");
        }
    }

    private void checkIfUserHasCheckedIn(User user, Event event) {
        if (event.getUsersWithPresence().contains(user.getId())) {
            throw new UserCheckedInException("Cancellation not allowed, user has already checked in!");
        };
    }

    private void checkIfUserAlreadyRegisteredPresence(Event event, User user) {
        if (event.getUsersWithPresence().contains(user.getId())) {
            throw new UserAlreadyRegisteredPresenceException("User presence already registered for this event!");
        }
    }

    private void checkIfWithinAllowedTimeframe(Event event) {
        LocalDateTime timeNow = LocalDateTime.now();
        if (timeNow.isBefore(event.getStartDate().minusHours(1)) || timeNow.isAfter(event.getEndDate())) {
            throw new EventTimeframeException("Registration of presence is not allowed outside the event timeframe!");
        }
    }

    private void checkIfEventAlreadyStarted(Event event) {
        if (LocalDateTime.now().isAfter(event.getStartDate())) {
            throw new EventAlreadyStartedException("Event already started!");
        }
    }

    private void checkIfUserAlreadyRegisteredForEvent(User user, Event event) {
        if (event.getUsers().contains(user)) {
            throw new UserAlreadyRegisteredForEventException("User already registered for event!");
        }
    }

    private void checkIfUserAlreadyHasReservation(User user, Event event) {
        if (reservationRepository.findByUserAndEvent(user, event).isPresent()) {
            throw new UserAlreadyHasReservationException("User already has a reservation for this event!");
        }
    }

    private Reservation findReservationById(Long reserveId) {
        return reservationRepository.findById(reserveId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found!"));
    }

    private void removeUser(User user, Event event) {
        event.getUsers().remove(user);
        eventRepository.save(event);
    }
}
