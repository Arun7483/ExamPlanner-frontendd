package com.cts.Backend_ExamPlanner.entities;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "departments")
@Data
public class Department {
    @Id
    @Column(name = "departmentId")
    private String departmentId;

    @Column(nullable = false, unique = true) // This is the reference point for Courses
    private String departmentCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String headOfDepartment;

    // Renamed for clarity: Total semesters in the department (e.g., 8)
    private Integer totalSemesters;
}