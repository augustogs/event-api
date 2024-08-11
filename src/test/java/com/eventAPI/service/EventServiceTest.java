package com.eventAPI.service;

import com.eventAPI.dto.EventDTO;
import com.eventAPI.exception.EventAlreadyRegisteredExecption;
import com.eventAPI.model.Event;
import com.eventAPI.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEventSuccess() {
        EventDTO eventDTO = new EventDTO(null, "Conference", 100, LocalDateTime.of(2024, 8, 10, 10, 0), LocalDateTime.of(2024, 8, 10, 18, 0));
        Event eventToSave = new Event(eventDTO.getName(), eventDTO.getVacancies(), eventDTO.getStartDate(), eventDTO.getEndDate());
        Event savedEvent = new Event(eventDTO.getName(), eventDTO.getVacancies(), eventDTO.getStartDate(), eventDTO.getEndDate());
        savedEvent.setId(1L);

        when(eventRepository.findByNameAndStartDate(eventDTO.getName(), eventDTO.getStartDate())).thenReturn(Optional.empty());
        when(eventRepository.save(eventToSave)).thenReturn(savedEvent);

        EventDTO createdEvent = eventService.createEvent(eventDTO);

        assertNotNull(createdEvent.getId());
        assertEquals("Conference", createdEvent.getName());
        assertEquals(100, createdEvent.getVacancies());
        assertEquals(LocalDateTime.of(2024, 8, 10, 10, 0), createdEvent.getStartDate());
        assertEquals(LocalDateTime.of(2024, 8, 10, 18, 0), createdEvent.getEndDate());

        verify(eventRepository, times(1)).findByNameAndStartDate(eventDTO.getName(), eventDTO.getStartDate());
        verify(eventRepository, times(1)).save(eventToSave);
    }

    @Test
    void testCreateEventAlreadyRegistered() {
        EventDTO eventDTO = new EventDTO(null, "Conference", 100, LocalDateTime.of(2023, 12, 15, 10, 0), LocalDateTime.of(2023, 12, 15, 18, 0));
        Event existingEvent = new Event(eventDTO.getName(), eventDTO.getVacancies(), eventDTO.getStartDate(), eventDTO.getEndDate());

        when(eventRepository.findByNameAndStartDate(eventDTO.getName(), eventDTO.getStartDate())).thenReturn(Optional.of(existingEvent));

        EventAlreadyRegisteredExecption exception = assertThrows(EventAlreadyRegisteredExecption.class, () -> {
            eventService.createEvent(eventDTO);
        });

        assertEquals("Event with the same name and start date already exists!", exception.getMessage());

        verify(eventRepository, times(1)).findByNameAndStartDate(eventDTO.getName(), eventDTO.getStartDate());
        verify(eventRepository, times(0)).save(any(Event.class));
    }
}
