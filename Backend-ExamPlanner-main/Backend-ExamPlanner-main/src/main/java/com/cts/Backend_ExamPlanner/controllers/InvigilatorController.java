package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.Services.InvigilatorService;
import com.cts.Backend_ExamPlanner.dto.InvigilatorRequestDTO;
import com.cts.Backend_ExamPlanner.entities.Invigilator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invigilators")
@CrossOrigin(origins = "${app.cors.allowed-origin}")
class InvigilatorController {

    @Autowired
    private InvigilatorService invigilatorService;

    @GetMapping
    public Page<Invigilator> getInvigilators(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return invigilatorService.getPaginatedInvigilator(page, size);
    }

    @PostMapping("/add")
    public ResponseEntity<Invigilator> addInvigilator(@Valid @RequestBody InvigilatorRequestDTO dto) {
        Invigilator created = invigilatorService.createInvigilator(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/update")
    public ResponseEntity<Invigilator> updateInvigilator(@Valid @RequestBody InvigilatorRequestDTO dto){
        Invigilator updated = invigilatorService.updateInvigilator(dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvigilator(@PathVariable String id) {
        invigilatorService.deleteInvigilaor(id);
        return ResponseEntity.noContent().build();
    }
}