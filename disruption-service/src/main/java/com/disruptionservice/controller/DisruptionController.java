package com.disruptionservice.controller;

import com.disruptionservice.dto.CreateDisruptionRequest;
import com.disruptionservice.entity.DisruptionEvent;
import com.disruptionservice.service.DisruptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/disruptions")
public class DisruptionController {

    private final DisruptionService disruptionService;

    @Autowired
    public DisruptionController(DisruptionService disruptionService) {
        this.disruptionService = disruptionService;
    }

    @PostMapping
    public ResponseEntity<DisruptionEvent> logDisruption(@Valid @RequestBody CreateDisruptionRequest request) {
        return new ResponseEntity<>(disruptionService.logDisruption(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DisruptionEvent>> getAllDisruptions() {
        return ResponseEntity.ok(disruptionService.getAllDisruptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisruptionEvent> getDisruptionById(@PathVariable UUID id) {
        return ResponseEntity.ok(disruptionService.getDisruptionById(id));
    }
}