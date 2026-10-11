package com.cts.Backend_ExamPlanner.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentDTO {
    @NotBlank(message = "Student ID is required")
    private String studentID;

    @NotBlank(message = "Student name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String studentName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String studentEmail;

    @NotBlank(message = "Department code is required")
    private String departmentCode;

    @NotNull(message = "Current semester is required")
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 8, message = "Semester cannot exceed 8")
    private Integer currentSemester;
}
