package com.supplierservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID facilityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    @JsonIgnore
    private Supplier supplier;

    private BigDecimal geoLatitude;
    private BigDecimal geoLongitude;

    @Column(length = 20)
    private String regionCode;

    private Integer capacityUnitsDay;
}