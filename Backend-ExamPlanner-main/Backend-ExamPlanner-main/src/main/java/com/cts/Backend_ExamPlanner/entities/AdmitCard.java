package com.cts.Backend_ExamPlanner.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="admit_card")
@Data
public class AdmitCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentId;
    private Long examScheduleId;
    private LocalDateTime downloadTime;

}