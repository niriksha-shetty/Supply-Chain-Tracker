package com.disruptionservice.repository;

import com.disruptionservice.entity.DisruptionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DisruptionEventRepository extends JpaRepository<DisruptionEvent, UUID> {
}