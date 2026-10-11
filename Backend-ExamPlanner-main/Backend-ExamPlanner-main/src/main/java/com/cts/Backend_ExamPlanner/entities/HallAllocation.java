package com.cts.Backend_ExamPlanner.entities;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="Halls")
@Data
public class HallAllocation {

    @Id
    @Column(name="hall_id")
    private String hallId;

    @Column(name="capacity")
    private Integer capacity;

    @Column(name="floor",nullable = false)
    private String floor;

    @Enumerated(EnumType.STRING)
    @Column(name="availability",nullable = false)
    private Availability availability;
}
