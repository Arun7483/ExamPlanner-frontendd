package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.dto.ReportDTO;
import com.cts.Backend_ExamPlanner.Services.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invigilators/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService)
    {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAll() {
        return ResponseEntity.ok(reportService.findAll());
    }

    @PostMapping
    public ResponseEntity<ReportDTO> create(@RequestBody ReportDTO dto) {
        ReportDTO created = reportService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("Invalid report ID");
        }
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ReportDTO>> saveBulk(@RequestBody List<ReportDTO> dtos) {
        List<ReportDTO> created = reportService.saveBulk(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ReportDTO>> getByStudent(@PathVariable String studentId) {
        if (!StringUtils.hasText(studentId)) {
            return ResponseEntity.badRequest().build();
        }

        List<ReportDTO> result;
        result = reportService.getByStudent(studentId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ReportDTO>> getByExam(@PathVariable Long examId){
        if (examId == null || examId <= 0) {
            return ResponseEntity.badRequest().build();
        }

        List<ReportDTO> result = reportService.getByExam(examId);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<Void> deleteByStudent(@PathVariable String studentId) {
        if (!StringUtils.hasText(studentId)) {
            return ResponseEntity.badRequest().build();
        }
        reportService.deleteByStudent(studentId);

        return ResponseEntity.noContent().build();
    }

    private boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }
}