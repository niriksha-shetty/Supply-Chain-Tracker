package com.orderservice.controller;

import com.orderservice.dto.CreateOrderRequest;
import com.orderservice.entity.PurchaseOrder;
import com.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<PurchaseOrder> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PurchaseOrder>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PatchMapping("/flag-facility/{facilityId}/{status}")
    public ResponseEntity<String> flagOrdersByFacility(
            @PathVariable("facilityId") UUID facilityId,
            @PathVariable("status") String status) {
        orderService.updateOrderStatusByFacility(facilityId, status);
        return ResponseEntity.ok("Orders updated to " + status);
    }
}