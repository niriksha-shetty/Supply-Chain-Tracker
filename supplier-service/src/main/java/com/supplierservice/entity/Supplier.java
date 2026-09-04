package com.supplierservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "suppliers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID supplierId;

    @Column(nullable = false)
    private String legalName;

    @Min(1)
    @Max(5)
    private short tierLevel;

    @Builder.Default
    private BigDecimal esgComplianceScore = BigDecimal.valueOf(100.00);

    private String status;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Facility> facilities = new ArrayList<>();

    public void addFacility(Facility facility) {
        facilities.add(facility);
        facility.setSupplier(this);
    }
}