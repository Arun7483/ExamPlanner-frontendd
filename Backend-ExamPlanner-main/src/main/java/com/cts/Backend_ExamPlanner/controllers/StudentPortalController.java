package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.dto.StudentExamViewDTO;
import com.cts.Backend_ExamPlanner.entities.Student;
import com.cts.Backend_ExamPlanner.entities.AdmitCard;
import com.cts.Backend_ExamPlanner.repositories.AdmitCardDownloadRepository;
import com.cts.Backend_ExamPlanner.repositories.StudentPortalRepository;
import com.cts.Backend_ExamPlanner.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/student-portal")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentPortalController {

    @Autowired
    private StudentPortalRepository studentPortalRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdmitCardDownloadRepository admitCardRepository; // Using your created repository

    @GetMapping("/exams")
    public ResponseEntity<List<StudentExamViewDTO>> getMySchedule(Authentication authentication) {
        String email = authentication.getName();
        // Fetches full details like Hall ID and Floor
        List<StudentExamViewDTO> schedule = studentPortalRepository.findAdmitCardDetailsByEmail(email);
        return ResponseEntity.ok(schedule);
    }
    @PostMapping("/track-download/{scheduleId}")
    public ResponseEntity<String> recordDownload(@PathVariable Long scheduleId, Authentication auth) {
        String email = auth.getName(); // This is 'student@gmail.com'

        // Use your custom query to find by Email, not ID
        Student student = studentRepository.findByStudentEmail(email);

        if (student == null) {
            return ResponseEntity.status(500).body("Student not found for email: " + email);
        }

        AdmitCard record = new AdmitCard();
        record.setStudentId(student.getStudentID()); // This sets 'S1221'
        record.setExamScheduleId(scheduleId);
        record.setDownloadTime(LocalDateTime.now());

        admitCardRepository.save(record);
        return ResponseEntity.ok("Download recorded for student: " + student.getStudentID());
    }
}