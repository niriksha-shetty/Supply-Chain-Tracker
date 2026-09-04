package com.disruptionservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class CreateDisruptionRequest {

    @NotBlank(message = "eventType is required")
    private String eventType;

    @Min(1) @Max(10)
    private Integer severityIndex;

    private String sourceRegionCode;
    private List<CreateImpactRequest> impacts;

    public CreateDisruptionRequest() {}

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Integer getSeverityIndex() { return severityIndex; }
    public void setSeverityIndex(Integer severityIndex) { this.severityIndex = severityIndex; }

    public String getSourceRegionCode() { return sourceRegionCode; }
    public void setSourceRegionCode(String sourceRegionCode) { this.sourceRegionCode = sourceRegionCode; }

    public List<CreateImpactRequest> getImpacts() { return impacts; }
    public void setImpacts(List<CreateImpactRequest> impacts) { this.impacts = impacts; }
}