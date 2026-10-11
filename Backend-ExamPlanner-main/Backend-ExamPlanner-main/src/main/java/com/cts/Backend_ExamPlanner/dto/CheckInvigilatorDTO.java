package com.cts.Backend_ExamPlanner.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CheckInvigilatorDTO {
    @NotBlank(message = "Department code is required")
    private String deptCode;
    @NotNull(message = "Exam date is required")
    @FutureOrPresent(message = "Exam date cannot be in the past")
    private LocalDate examDate;
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    @Positive(message = "Duration must be a positive number of minutes")
    @Max(value = 480, message = "Duration cannot exceed 8 hours (480 minutes)")
    private int duration;
}
