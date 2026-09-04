package com.orderservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CreateLineItemRequest {

    private UUID partId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public CreateLineItemRequest() {
    }

    public UUID getPartId() {
        return partId;
    }

    public void setPartId(UUID partId) {
        this.partId = partId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}