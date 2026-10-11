package com.cts.Backend_ExamPlanner.dto;

import lombok.Data;

@Data
public class DashboardDTO {
    private Long totalStudents;
    private Long totalExamsScheduled;
    private Long totalInvigilators;
    private Long totalHalls;
}
