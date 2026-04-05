package com.courier.dto;

import com.courier.model.CourierStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {

    @NotNull(message = "Status is required")
    private CourierStatus status;

    public StatusUpdateRequest() {}

    public StatusUpdateRequest(CourierStatus status) {
        this.status = status;
    }

    public CourierStatus getStatus() { return status; }
    public void setStatus(CourierStatus status) { this.status = status; }
}
