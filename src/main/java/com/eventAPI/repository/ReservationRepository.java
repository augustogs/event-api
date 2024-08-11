package com.eventAPI.repository;

import com.eventAPI.model.Event;
import com.eventAPI.model.Reservation;
import com.eventAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByUserAndEvent(User user, Event event);
}
