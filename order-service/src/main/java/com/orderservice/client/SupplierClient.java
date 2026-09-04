package com.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

// Points directly to the Supplier Service running on port 8081
@FeignClient(name = "supplier-service", url = "http://localhost:8081/api/suppliers")
public interface SupplierClient {

    @GetMapping("/{id}")
    SupplierDTO getSupplierById(@PathVariable("id") UUID id);
}