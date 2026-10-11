package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.Services.HallAllocationService;
import com.cts.Backend_ExamPlanner.entities.HallAllocation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@CrossOrigin(origins = "http://localhost:4200") // adjust if needed
class HallAllocationController {

    private final HallAllocationService service;

    public HallAllocationController(HallAllocationService service) {
        this.service = service;
    }

    /** GET /api/halls */
    @GetMapping
    public ResponseEntity<List<HallAllocation>> listAll() {
        return ResponseEntity.ok(service.getAll());
    }

    /** POST /api/halls  (create) */
    @PostMapping
    public ResponseEntity<HallAllocation> create(@Validated @RequestBody HallAllocation hall) {
        HallAllocation saved = service.save(hall);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** PUT /api/halls/{hallId}  (update) */
    @PutMapping("/{hallId}")
    public ResponseEntity<HallAllocation> update(
            @PathVariable String hallId,
            @RequestBody HallAllocation hallDetails
    ) {
        HallAllocation updated = service.update(hallId, hallDetails);
        return ResponseEntity.ok(updated);
    }

    /** DELETE /api/halls/{hallId}  (delete) */
    @DeleteMapping("/{hallId}")
    public ResponseEntity<Void> delete(@PathVariable String hallId) {
        service.delete(hallId);
        return ResponseEntity.noContent().build();
    }

}