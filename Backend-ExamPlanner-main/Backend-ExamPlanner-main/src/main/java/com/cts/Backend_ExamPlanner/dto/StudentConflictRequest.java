package com.cts.Backend_ExamPlanner.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class StudentConflictRequest {
    @NotBlank(message = "Department code is required")
    private String departmentCode;
    @NotNull(message = "Semester is required")
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 8, message = "Semester cannot exceed 8")
    private Integer semester;
    @NotNull(message = "Exam date is required")
    @FutureOrPresent(message = "Conflict check must be for a current or future date")
    private LocalDate examDate;
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be a positive number of minutes")
    private Integer duration; // in minutes
}
