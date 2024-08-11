package com.eventAPI.dto;

import java.time.LocalDateTime;

public class EventDTO {

    private Long id;
    private String name;
    private Integer vacancies;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public EventDTO(Long id, String name, Integer vacancies, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.vacancies = vacancies;
        this.startDate = startDate;
        this.endDate = endDate;
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
}
