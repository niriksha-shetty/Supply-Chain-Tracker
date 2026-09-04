package com.supplierservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateFacilityRequest {
    private BigDecimal geoLatitude;
    private BigDecimal geoLongitude;
    private String regionCode;
    private Integer capacityUnitsDay;
}
