package com.orderservice.repository;

import com.orderservice.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, UUID> {
    List<PurchaseOrder> findByDestFacilityId(UUID destFacilityId);

    @Modifying
    @Transactional
    @Query("UPDATE PurchaseOrder p SET p.poStatus = :status WHERE p.destFacilityId = :facilityId")
    int updateOrderStatusByFacilityId(@Param("facilityId") UUID facilityId, @Param("status") String status);
}