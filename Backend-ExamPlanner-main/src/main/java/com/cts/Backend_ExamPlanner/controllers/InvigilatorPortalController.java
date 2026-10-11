package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.Services.UpcomingExamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invigilators/upcoming-exams")
@CrossOrigin(origins = "http://localhost:4200")
public class InvigilatorPortalController {

    private final UpcomingExamService service;

    public InvigilatorPortalController(UpcomingExamService service) {
        this.service = service;
    }

    @GetMapping
    public List<Map<String, String>> getExams(@RequestParam String email) {
        return service.getUpcomingExamsForEmail(email);
    }
}
