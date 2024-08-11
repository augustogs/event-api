package com.eventAPI.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "events", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "startDate"})})
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer vacancies;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToMany
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    @ElementCollection
    @CollectionTable(name = "event_user_presence", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "user_id")
    private Set<Long> usersWithPresence;

    @ElementCollection
    @CollectionTable(name = "event_user_reserve", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "user_id")
    private Set<Long> reserves;

    public Event() {
    }

    public Event(String name, Integer vacancies, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.vacancies = vacancies;
        this.startDate = startDate;
        this.endDate = endDate;
        this.users = new HashSet<>();
        this.usersWithPresence = new HashSet<>();
        this.reserves = new HashSet<>();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Integer getVacancies() {
        return this.vacancies;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void registerPresence(Long userId) {
        this.usersWithPresence.add(userId);
    }

    public Set<Long> getUsersWithPresence() {
        return this.usersWithPresence;
    }

    public void registerReserve(Long userId) {
        this.reserves.add(userId);
    }

    public Set<Long> getReserves() {
        return this.reserves;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public void removeReserve(Long userId) {
        this.reserves.remove(userId);
    }

    public void setStartDate(LocalDateTime localDateTime) {
        this.startDate = localDateTime;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }
}
