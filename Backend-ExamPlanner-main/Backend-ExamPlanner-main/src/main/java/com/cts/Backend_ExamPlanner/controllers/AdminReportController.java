package com.cts.Backend_ExamPlanner.controllers;

import com.cts.Backend_ExamPlanner.Services.ReportService;
import com.cts.Backend_ExamPlanner.dto.ReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Paged + filterable + sortable listing for dashboard
    @GetMapping("/paged")
    public ResponseEntity<Page<ReportDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        Page<ReportDTO> result = reportService.searchPaged(page, size, sort, direction, studentId, examId, from, to);
        return ResponseEntity.ok(result);
    }

    // Metrics tiles
    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> summary() {
        return ResponseEntity.ok(reportService.summaryMetrics());
    }

    @GetMapping("/metrics/by-exam")
    public ResponseEntity<List<Map<String, Object>>> byExam() {
        return ResponseEntity.ok(reportService.countByExam());
    }

    @GetMapping("/metrics/per-day")
    public ResponseEntity<List<Map<String, Object>>> perDay(@RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(reportService.countPerDaySince(days));
    }

    // CSV export
    @GetMapping(value = "/export", produces = "text/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=malpractice_reports.csv");
        List<ReportDTO> all = reportService.findAll();
        try (PrintWriter w = response.getWriter()) {
            w.println("id,examId,studentId,reportName,reportDescription,createdAt");
            for (ReportDTO r : all) {
                String desc = r.getReportDescription() == null ? "" : r.getReportDescription().replace("\"", "\"\"");
                // quote potentially comma-containing fields
                w.printf("%d,%d,%s,\"%s\",\"%s\",%s%n",
                        r.getId(),
                        r.getExamId(),
                        r.getStudentId(),
                        r.getReportName(),
                        desc,
                        r.getCreatedAt());
            }
        }
    }
}