package com.disruptionservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "facility_impacts")
public class FacilityImpact {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID impactId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private DisruptionEvent event;

    private UUID facilityId; // Logical reference to supplier_db
    private Integer estDelayDays;
    private String status;

    public FacilityImpact() {
    }

    public UUID getImpactId() { return impactId; }
    public void setImpactId(UUID impactId) { this.impactId = impactId; }

    public DisruptionEvent getEvent() { return event; }
    public void setEvent(DisruptionEvent event) { this.event = event; }

    public UUID getFacilityId() { return facilityId; }
    public void setFacilityId(UUID facilityId) { this.facilityId = facilityId; }

    public Integer getEstDelayDays() { return estDelayDays; }
    public void setEstDelayDays(Integer estDelayDays) { this.estDelayDays = estDelayDays; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}