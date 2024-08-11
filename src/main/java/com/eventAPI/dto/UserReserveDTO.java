package com.eventAPI.dto;

public class UserReserveDTO {

    private Long id;
    private String userEmail;
    private Long eventId;

    public UserReserveDTO() {
    }

    public UserReserveDTO(String userEmail, Long eventId) {
        this.userEmail = userEmail;
        this.eventId = eventId;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public Long getEventId() {
        return this.eventId;
    }
}
