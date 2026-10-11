package com.cts.Backend_ExamPlanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class InvigilatorAttendanceDTO {
    @NotBlank(message = "Student ID is required")
    private String studentId;
    @NotBlank(message = "Student name cannot be empty")
    private String studentName;
    @NotBlank(message = "Seat number is required")
    // Optional: Use a pattern if seat numbers follow a format like 'A-101'
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Invalid seat number format")
    private String seatNumber;
    @NotBlank(message = "Department is required")
    private String department;
}
