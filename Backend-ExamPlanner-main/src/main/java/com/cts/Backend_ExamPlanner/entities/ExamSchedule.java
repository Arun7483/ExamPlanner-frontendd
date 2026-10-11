package com.cts.Backend_ExamPlanner.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "exam_schedules")
@Data
public class ExamSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    private Course course;

    @OneToMany(mappedBy = "examSchedule", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ExamHallAssignment> hallAssignments;

    @Column(nullable = false)
    private LocalDate examDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime; //calculated field

    @Column(nullable = false)
    private Integer durationMinutes;

    private String departmentCode;
    private Integer semester;
}
