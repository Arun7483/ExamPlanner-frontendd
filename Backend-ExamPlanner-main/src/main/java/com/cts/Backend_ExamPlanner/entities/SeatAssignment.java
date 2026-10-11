package com.cts.Backend_ExamPlanner.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="seat_assignments")
@Data
public class SeatAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "hall_assignment_id")
    private ExamHallAssignment hallAssignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String seatNumber;
}
