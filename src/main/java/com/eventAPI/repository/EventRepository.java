package com.eventAPI.repository;

import com.eventAPI.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByNameAndStartDate(String name, LocalDateTime startDate);
}
