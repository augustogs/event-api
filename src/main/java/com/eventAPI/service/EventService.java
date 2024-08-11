package com.eventAPI.service;

import com.eventAPI.dto.EventDTO;
import com.eventAPI.exception.EventAlreadyRegisteredExecption;
import com.eventAPI.model.Event;
import com.eventAPI.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        Optional<Event> eventOpt = eventRepository.findByNameAndStartDate(eventDTO.getName(), eventDTO.getStartDate());
        if (eventOpt.isPresent()) {
            throw new EventAlreadyRegisteredExecption("Event with the same name and start date already exists!");
        }
        Event event = new Event(eventDTO.getName(), eventDTO.getVacancies(), eventDTO.getStartDate(), eventDTO.getEndDate());
        event = eventRepository.save(event);
        return new EventDTO(event.getId(), event.getName(), event.getVacancies(), event.getStartDate(), event.getEndDate());
    }
}
