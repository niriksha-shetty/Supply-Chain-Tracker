package com.disruptionservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderClientFallbackFactory implements FallbackFactory<OrderClient> {

    private static final Logger log = LoggerFactory.getLogger(OrderClientFallbackFactory.class);

    @Override
    public OrderClient create(Throwable cause) {
        return new OrderClient() {
            @Override
            public String flagOrdersByFacility(UUID facilityId, String status) {
                log.warn("Fallback triggered for OrderClient.flagOrdersByFacility({}, {}). Cause: {}",
                        facilityId, status, cause.getMessage());
                return "Order Service is currently unavailable. Flagging request for facility ID "
                        + facilityId + " has been queued/fallback recorded.";
            }
        };
    }
}
