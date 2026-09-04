package com.supplierservice.service;

import com.supplierservice.dto.CreateSupplierRequest;
import com.supplierservice.entity.Facility;
import com.supplierservice.entity.Supplier;
import com.supplierservice.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    // Explicit constructor injection instead of Lombok @RequiredArgsConstructor
    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier createSupplier(CreateSupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setLegalName(request.getLegalName());
        supplier.setTierLevel(request.getTierLevel());
        supplier.setEsgComplianceScore(request.getEsgComplianceScore());
        supplier.setStatus(request.getStatus());

        if (request.getFacilities() != null) {
            for (var facReq : request.getFacilities()) {
                Facility facility = new Facility();
                facility.setGeoLatitude(facReq.getGeoLatitude());
                facility.setGeoLongitude(facReq.getGeoLongitude());
                facility.setRegionCode(facReq.getRegionCode());
                facility.setCapacityUnitsDay(facReq.getCapacityUnitsDay());

                supplier.addFacility(facility);
            }
        }
        return supplierRepository.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(UUID id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with ID: " + id));
    }

    public void deleteSupplier(UUID id) {
        supplierRepository.deleteById(id);
    }
}