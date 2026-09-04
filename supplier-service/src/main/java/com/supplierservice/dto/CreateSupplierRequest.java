package com.supplierservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateSupplierRequest {
    @NotBlank(message = "Legal name cannot be blank")
    private String legalName;

    @Min(1)
    @Max(5)
    private short tierLevel;

    private BigDecimal esgComplianceScore;
    private String status;
    private List<CreateFacilityRequest> facilities;
}