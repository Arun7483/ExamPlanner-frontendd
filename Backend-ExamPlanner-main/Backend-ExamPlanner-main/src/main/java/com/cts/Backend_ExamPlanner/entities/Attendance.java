package com.cts.Backend_ExamPlanner.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="hall_id", nullable = false)
    private String hallId;

    @Column(name="exam_id",nullable = false)
    private Long examId;

    @Column(name = "student_id",nullable = false)
    private String studentId;

    @Column(nullable = false)
    private Boolean present;

    @Column(name = "marked_at")
    private LocalDateTime markedAt = LocalDateTime.now();


    @PrePersist
    public void onCreate(){
        this.markedAt=LocalDateTime.now();
    }

}