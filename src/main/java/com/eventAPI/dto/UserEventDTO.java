package com.eventAPI.dto;

public class UserEventDTO {

    private String userEmail;
    private Long eventId;

    public UserEventDTO() {
    }

    public UserEventDTO(String userEmail, Long eventId) {
        this.userEmail = userEmail;
        this.eventId = eventId;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }
}
