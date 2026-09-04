package com.disruptionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "order-service", url = "http://localhost:8082/api/orders")
public interface OrderClient {

    @PatchMapping("/flag-facility/{facilityId}/{status}")
    String flagOrdersByFacility(
            @PathVariable("facilityId") UUID facilityId,
            @PathVariable("status") String status
    );
}