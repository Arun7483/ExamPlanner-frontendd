package com.cts.Backend_ExamPlanner.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class InvigilatorExamViewDTO {
    @NotNull(message = "Exam ID is required")
    private Long examId;
    @NotBlank(message = "Course name is required")
    private String courseName;
    @NotNull(message = "Exam date is required")
    @FutureOrPresent(message = "Exam date cannot be in the past")
    private LocalDate date;
    @NotNull(message = "Start time is required")
    private LocalTime startTime;
    @NotNull(message = "End time is required")
    private LocalTime endTime;
    @NotBlank(message = "Hall ID is required")
    private String hallId;
    @NotBlank(message = "Floor description is required")
    private String floor;
}
