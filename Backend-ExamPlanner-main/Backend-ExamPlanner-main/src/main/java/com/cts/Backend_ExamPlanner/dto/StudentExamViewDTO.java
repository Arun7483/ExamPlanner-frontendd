package com.cts.Backend_ExamPlanner.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor // Required for standard JPA operations
public class StudentExamViewDTO {
    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Student ID is required")
    private String studentID;

    @NotBlank(message = "Department name is required")
    private String deptName;

    @NotNull(message = "Semester is required")
    @Min(1) @Max(8)
    private Integer semester;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotNull(message = "Exam date is required")
    private LocalDate date;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Hall ID is required")
    private String hallId;

    @NotBlank(message = "Floor is required")
    private String floor;

    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    @NotNull(message = "Exam schedule ID is required")
    private Long examScheduleId;

    // The order of these parameters MUST match your Repository @Query exactly
    public StudentExamViewDTO(String studentName, String studentID, String deptName, Integer semester,
                              String courseName, LocalDate date, LocalTime startTime, LocalTime endTime,
                              String hallId, String floor, String seatNumber, Long examScheduleId) {
        this.studentName = studentName;
        this.studentID = studentID;
        this.deptName = deptName;
        this.semester = semester;
        this.courseName = courseName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hallId = hallId;
        this.floor = floor;
        this.seatNumber = seatNumber;
        this.examScheduleId = examScheduleId;
    }
}