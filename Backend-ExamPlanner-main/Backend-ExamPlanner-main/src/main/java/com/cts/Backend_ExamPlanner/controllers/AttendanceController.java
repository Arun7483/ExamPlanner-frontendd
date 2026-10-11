package com.cts.Backend_ExamPlanner.controllers;
import com.cts.Backend_ExamPlanner.Services.AttendanceService;
import com.cts.Backend_ExamPlanner.dto.AttendanceRequestDTO;
import com.cts.Backend_ExamPlanner.dto.AttendanceResponseDTO;
import com.cts.Backend_ExamPlanner.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {


    @Autowired
    private AttendanceService service;

    @PostMapping("/submit")
    public ResponseEntity<String> markAttendance(
            @RequestBody AttendanceRequestDTO request) {
        try {
            service.saveBulkAttendance(request);
            return ResponseEntity.ok("Attendance processed successfully.");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing attendance "+ e.getMessage());
        }
    }

    @GetMapping("/{examId}/{hallId}")
    public ResponseEntity<List<AttendanceResponseDTO>> getAttendance(
            @PathVariable Long examId,
            @PathVariable String hallId) {
        try {
            List<AttendanceResponseDTO> attendanceList = service.getAttendanceByExamAndHall(examId, hallId);
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}