package com.cts.Backend_ExamPlanner.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDTO {

    private Long id;
    private Long examId;
    private String studentId;
    private String reportName;
    private String reportDescription;
    private LocalDateTime createdAt;

}
