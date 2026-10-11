package com.cts.Backend_ExamPlanner.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseDTO {
    @NotBlank(message = "Course ID is required")
    @Size(min = 3, max = 10, message = "Course ID must be between 3 and 10 characters")
    private String id;
    @NotBlank(message = "Course name cannot be empty")
    private String courseName;
    @NotBlank(message = "Credits are required")
    @Pattern(regexp = "^\\d+$", message = "Credits must be a numeric value")
    private String credits;
    @NotBlank(message = "Department code is required")
    private String departmentCode;
    @NotNull(message = "Semester is required")
    @Min(value = 1, message = "Semester must be at least 1")
    @Max(value = 8, message = "Semester cannot exceed 8")
    private Integer semester;
}
