package com.eventAPI.dto;

public class ReserveIdDTO {

    private Long reserveId;

    public ReserveIdDTO(Long id) {
        this.reserveId = id;
    }

    public Long getReserveId() {
        return reserveId;
    }

    public void setReserveId(Long reserveId) {
        this.reserveId = reserveId;
    }
}
