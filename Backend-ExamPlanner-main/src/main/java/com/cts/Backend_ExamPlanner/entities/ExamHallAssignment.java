package com.cts.Backend_ExamPlanner.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "exam_hall_assignments")
@Data
public class ExamHallAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_schedule_id")
    @JsonIgnore
    private ExamSchedule examSchedule;

    @OneToMany(mappedBy = "hallAssignment",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<SeatAssignment> seatAssignments;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private HallAllocation hall;

    @ManyToOne
    @JoinColumn(name = "invigilator_id")
    private Invigilator invigilator;
}
