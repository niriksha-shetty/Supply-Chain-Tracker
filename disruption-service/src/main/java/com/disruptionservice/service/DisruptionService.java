package com.disruptionservice.service;

import com.disruptionservice.client.OrderClient;
import com.disruptionservice.dto.CreateDisruptionRequest;
import com.disruptionservice.entity.DisruptionEvent;
import com.disruptionservice.entity.FacilityImpact;
import com.disruptionservice.repository.DisruptionEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DisruptionService {

    private final DisruptionEventRepository repository;
    private final OrderClient orderClient;

    @Autowired
    public DisruptionService(DisruptionEventRepository repository, OrderClient orderClient) {
        this.repository = repository;
        this.orderClient = orderClient;
    }

    public DisruptionEvent logDisruption(CreateDisruptionRequest request) {
        DisruptionEvent event = new DisruptionEvent();
        event.setEventType(request.getEventType());
        event.setSeverityIndex(request.getSeverityIndex());
        event.setSourceRegionCode(request.getSourceRegionCode());
        event.setTimestampDetected(LocalDateTime.now());

        if (request.getImpacts() != null) {
            for (var impReq : request.getImpacts()) {
                FacilityImpact impact = new FacilityImpact();
                impact.setFacilityId(impReq.getFacilityId());
                impact.setEstDelayDays(impReq.getEstDelayDays());
                impact.setStatus(impReq.getStatus());
                event.addImpact(impact);

                // INTER-SERVICE COMMUNICATION:
                // Notify Order Service to flag all orders using this facility as DELAYED_AT_RISK
                try {
                    orderClient.flagOrdersByFacility(impReq.getFacilityId(), "DELAYED_AT_RISK");
                } catch (Exception e) {
                    System.err.println("Failed to notify Order Service for facility ID " + impReq.getFacilityId() + ": " + e.getMessage());
                }
            }
        }

        return repository.save(event);
    }

    public List<DisruptionEvent> getAllDisruptions() {
        return repository.findAll();
    }

    public DisruptionEvent getDisruptionById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disruption not found with ID: " + id));
    }
}