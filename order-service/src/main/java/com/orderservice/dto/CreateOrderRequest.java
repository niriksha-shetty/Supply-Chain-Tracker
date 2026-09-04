package com.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CreateOrderRequest {

    @NotNull(message = "supplierId is required")
    private UUID supplierId;

    private UUID destFacilityId;
    private LocalDateTime expectedDelivery;
    private List<CreateLineItemRequest> lineItems;

    public CreateOrderRequest() {
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public UUID getDestFacilityId() {
        return destFacilityId;
    }

    public void setDestFacilityId(UUID destFacilityId) {
        this.destFacilityId = destFacilityId;
    }

    public LocalDateTime getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(LocalDateTime expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public List<CreateLineItemRequest> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<CreateLineItemRequest> lineItems) {
        this.lineItems = lineItems;
    }
}