package com.orderservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID poId;

    @Column(nullable = false)
    private UUID supplierId;

    private UUID destFacilityId;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDelivery;
    private String poStatus;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POLineItem> lineItems = new ArrayList<>();

    public PurchaseOrder() {}

    public void addLineItem(POLineItem item) {
        lineItems.add(item);
        item.setPurchaseOrder(this);
    }

    public UUID getPoId() { return poId; }
    public void setPoId(UUID poId) { this.poId = poId; }

    public UUID getSupplierId() { return supplierId; }
    public void setSupplierId(UUID supplierId) { this.supplierId = supplierId; }

    public UUID getDestFacilityId() { return destFacilityId; }
    public void setDestFacilityId(UUID destFacilityId) { this.destFacilityId = destFacilityId; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public LocalDateTime getExpectedDelivery() { return expectedDelivery; }
    public void setExpectedDelivery(LocalDateTime expectedDelivery) { this.expectedDelivery = expectedDelivery; }

    public String getPoStatus() { return poStatus; }
    public void setPoStatus(String poStatus) { this.poStatus = poStatus; }

    public List<POLineItem> getLineItems() { return lineItems; }
    public void setLineItems(List<POLineItem> lineItems) { this.lineItems = lineItems; }
}