package com.cts.Backend_ExamPlanner.controllers;


import com.cts.Backend_ExamPlanner.Services.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping()
    public ResponseEntity<?> getDashboardContent() {
        return ResponseEntity.ok(dashboardService);
    }
}
