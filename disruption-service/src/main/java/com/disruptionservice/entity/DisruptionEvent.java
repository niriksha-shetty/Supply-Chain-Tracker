package com.disruptionservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "disruption_events")
public class DisruptionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID eventId;

    private String eventType; // e.g., PORT_CLOSURE, SEVERE_WEATHER
    private Integer severityIndex; // 1 to 10
    private String sourceRegionCode; // e.g., IN-MH
    private LocalDateTime timestampDetected;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacilityImpact> impacts = new ArrayList<>();

    public DisruptionEvent() {
    }

    public void addImpact(FacilityImpact impact) {
        impacts.add(impact);
        impact.setEvent(this);
    }

    public UUID getEventId() { return eventId; }
    public void setEventId(UUID eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Integer getSeverityIndex() { return severityIndex; }
    public void setSeverityIndex(Integer severityIndex) { this.severityIndex = severityIndex; }

    public String getSourceRegionCode() { return sourceRegionCode; }
    public void setSourceRegionCode(String sourceRegionCode) { this.sourceRegionCode = sourceRegionCode; }

    public LocalDateTime getTimestampDetected() { return timestampDetected; }
    public void setTimestampDetected(LocalDateTime timestampDetected) { this.timestampDetected = timestampDetected; }

    public List<FacilityImpact> getImpacts() { return impacts; }
    public void setImpacts(List<FacilityImpact> impacts) { this.impacts = impacts; }
}