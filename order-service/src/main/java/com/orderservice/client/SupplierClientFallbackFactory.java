package com.orderservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SupplierClientFallbackFactory implements FallbackFactory<SupplierClient> {

    private static final Logger log = LoggerFactory.getLogger(SupplierClientFallbackFactory.class);

    @Override
    public SupplierClient create(Throwable cause) {
        return new SupplierClient() {
            @Override
            public SupplierDTO getSupplierById(UUID id) {
                log.warn("Fallback triggered for SupplierClient.getSupplierById({}). Cause: {}", id, cause.getMessage());
                SupplierDTO fallbackSupplier = new SupplierDTO();
                fallbackSupplier.setSupplierId(id);
                fallbackSupplier.setLegalName("Fallback Supplier (Service Unavailable)");
                fallbackSupplier.setTierLevel((short) 0);
                fallbackSupplier.setStatus("TEMPORARILY_UNAVAILABLE");
                return fallbackSupplier;
            }
        };
    }
}
