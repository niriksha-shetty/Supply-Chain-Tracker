package com.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "po_line_items")
public class POLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID lineItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "po_id")
    @JsonIgnore
    private PurchaseOrder purchaseOrder;

    private UUID partId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public POLineItem() {
    }

    public UUID getLineItemId() { return lineItemId; }
    public void setLineItemId(UUID lineItemId) { this.lineItemId = lineItemId; }

    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }
    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }

    public UUID getPartId() { return partId; }
    public void setPartId(UUID partId) { this.partId = partId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}