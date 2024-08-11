package com.eventAPI.dto;

public class UserPresenceDTO {

    private String userEmail;
    private Long eventId;

    public UserPresenceDTO(){
    }

    public UserPresenceDTO(String userEmail, Long eventId) {
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
