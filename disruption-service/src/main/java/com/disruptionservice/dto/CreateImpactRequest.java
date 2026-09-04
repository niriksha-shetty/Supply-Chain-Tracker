package com.disruptionservice.dto;

import java.util.UUID;

public class CreateImpactRequest {
    private UUID facilityId;
    private Integer estDelayDays;
    private String status;

    public CreateImpactRequest() {}

    public UUID getFacilityId() { return facilityId; }
    public void setFacilityId(UUID facilityId) { this.facilityId = facilityId; }

    public Integer getEstDelayDays() { return estDelayDays; }
    public void setEstDelayDays(Integer estDelayDays) { this.estDelayDays = estDelayDays; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}