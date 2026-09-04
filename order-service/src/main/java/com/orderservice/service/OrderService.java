package com.orderservice.service;

import com.orderservice.client.SupplierClient;
import com.orderservice.client.SupplierDTO;
import com.orderservice.dto.CreateOrderRequest;
import com.orderservice.entity.POLineItem;
import com.orderservice.entity.PurchaseOrder;
import com.orderservice.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final PurchaseOrderRepository orderRepository;
    private final SupplierClient supplierClient;

    @Autowired
    public OrderService(PurchaseOrderRepository orderRepository, SupplierClient supplierClient) {
        this.orderRepository = orderRepository;
        this.supplierClient = supplierClient;
    }

    public PurchaseOrder createOrder(CreateOrderRequest request) {
        try {
            SupplierDTO supplier = supplierClient.getSupplierById(request.getSupplierId());
            if (supplier == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Supplier does not exist!");
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Validation Failed: Supplier ID not found in Supplier Service! (" + request.getSupplierId() + ")");
        }

        PurchaseOrder order = new PurchaseOrder();
        order.setSupplierId(request.getSupplierId());
        order.setDestFacilityId(request.getDestFacilityId());
        order.setOrderDate(LocalDateTime.now());
        order.setExpectedDelivery(request.getExpectedDelivery() != null ? request.getExpectedDelivery() : LocalDateTime.now().plusDays(7));
        order.setPoStatus("CONFIRMED");

        if (request.getLineItems() != null) {
            for (var itemReq : request.getLineItems()) {
                POLineItem item = new POLineItem();
                item.setPartId(itemReq.getPartId() != null ? itemReq.getPartId() : UUID.randomUUID());
                item.setQuantity(itemReq.getQuantity());
                item.setUnitPrice(itemReq.getUnitPrice());
                order.addLineItem(item);
            }
        }

        return orderRepository.save(order);
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public PurchaseOrder getOrderById(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with ID: " + id));
    }

    @Transactional
    public void updateOrderStatusByFacility(UUID facilityId, String status) {
        orderRepository.updateOrderStatusByFacilityId(facilityId, status);
    }
}