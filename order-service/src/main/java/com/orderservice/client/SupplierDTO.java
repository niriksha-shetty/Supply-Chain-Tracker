package com.orderservice.client;

import lombok.Data;
import java.util.UUID;

@Data
public class SupplierDTO {
    private UUID supplierId;
    private String legalName;
    private short tierLevel;
    private String status;
}