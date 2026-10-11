package com.cts.Backend_ExamPlanner.dto;
import lombok.Data;

import java.util.List;


@Data
public class AttendanceRequestDTO {
    private Long examId;
    private String hallId;
    private List<StudentStatus> students;

    @Data
    public static class StudentStatus {
        private String id;
        private boolean present;
    }
}