package com.eventAPI.service;

import com.eventAPI.dto.*;
import com.eventAPI.exception.*;
import com.eventAPI.model.Event;
import com.eventAPI.model.Reservation;
import com.eventAPI.model.User;
import com.eventAPI.repository.EventRepository;
import com.eventAPI.repository.ReservationRepository;
import com.eventAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserEventServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private UserEventService userEventService;

    private User user;
    private Event event;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        user = new User("Test User", "test@example.com");
        event = new Event("Test Event", 10, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        reservation = new Reservation(user, event);
        user.setId(1L);
        event.setId(1L);
        reservation.setId(1L);
    }

    @Test
    void registerUserToEvent_UserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userEventService.registerUserToEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void registerUserToEvent_EventNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> userEventService.registerUserToEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void registerUserToEvent_UserAlreadyRegistered() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        event.addUser(user);

        assertThrows(UserAlreadyRegisteredException.class, () -> userEventService.registerUserToEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void registerUserToEvent_EventFullyBooked() {
        event.setVacancies(0);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(EventFullyBookException.class, () -> userEventService.registerUserToEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void registerUserToEvent_EventAlreadyStarted() {
        event.setStartDate(LocalDateTime.now().minusDays(1));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(EventAlreadyStartedException.class, () -> userEventService.registerUserToEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void removeUserFromEvent_UserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userEventService.removeUserFromEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void removeUserFromEvent_EventNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> userEventService.removeUserFromEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void removeUserFromEvent_UserNotRegisteredForEvent() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(UserNotRegisteredForEventException.class, () -> userEventService.removeUserFromEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void removeUserFromEvent_UserCheckedIn() {
        event.addUser(user);
        event.registerPresence(user.getId());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(UserCheckedInException.class, () -> userEventService.removeUserFromEvent(new UserEventDTO(user.getEmail(), event.getId())));
    }

    @Test
    void listUserRegistrations_UserNotRegisteredForAnyEvent() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(UserNotRegisteredForAnyEventException.class, () -> userEventService.listUserRegistrations(user.getId()));
    }

    @Test
    void listEventRegistrations_EventHasNoRegisteredUsers() {
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(EventHasNoRegisteredUsersException.class, () -> userEventService.listEventRegistrations(event.getId()));
    }

    @Test
    void registerUserPresence_EventNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> userEventService.registerUserPresence(new UserPresenceDTO(user.getEmail(), event.getId())));
    }

    @Test
    void registerUserPresence_UserNotRegisteredForEvent() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(UserNotRegisteredForEventException.class, () -> userEventService.registerUserPresence(new UserPresenceDTO(user.getEmail(), event.getId())));
    }

    @Test
    void registerUserPresence_AlreadyRegisteredPresence() {
        event.addUser(user);
        event.registerPresence(user.getId());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(UserAlreadyRegisteredPresenceException.class, () -> userEventService.registerUserPresence(new UserPresenceDTO(user.getEmail(), event.getId())));
    }

    @Test
    void registerUserPresence_OutsideAllowedTimeframe() {
        event.addUser(user);
        event.setStartDate(LocalDateTime.now().plusHours(2));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));

        assertThrows(EventTimeframeException.class, () -> userEventService.registerUserPresence(new UserPresenceDTO(user.getEmail(), event.getId())));
    }

    @Test
    void registerReserve_UserAlreadyHasReservation() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(eventRepository.findById(event.getId())).thenReturn(Optional.of(event));
        when(reservationRepository.findByUserAndEvent(user, event)).thenReturn(Optional.of(reservation));

        assertThrows(UserAlreadyHasReservationException.class, () -> userEventService.registerReserve(new UserReserveDTO(user.getEmail(), event.getId())));
    }

    @Test
    void convertReserveToRegistration_ReservationNotFound() {
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> userEventService.convertReserveToRegistration(new ReserveIdDTO(reservation.getId())));
    }
}
