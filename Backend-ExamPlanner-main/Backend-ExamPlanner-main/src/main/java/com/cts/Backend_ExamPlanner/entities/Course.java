package com.cts.Backend_ExamPlanner.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "courses")
public class Course {
    @Id
    private String id;
    private String name;
    private String credits;

    @ManyToOne
    @JoinColumn(name = "department_code", referencedColumnName = "departmentCode")
    private Department department;

    private Integer semester; // This represents the specific semester for the course (e.g., 3)
}
