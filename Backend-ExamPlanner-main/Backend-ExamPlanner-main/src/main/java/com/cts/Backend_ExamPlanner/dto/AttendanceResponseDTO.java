package com.cts.Backend_ExamPlanner.dto;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AttendanceResponseDTO {
    private Long id;
    private String studentId;
    private String studentName;
    private Boolean present;
    private LocalDateTime markedAt;


    public AttendanceResponseDTO(Long id,
                                 String studentId,
                                 String studentName,
                                 Boolean present,
                                 LocalDateTime markedAt) {
        this.id = id;
        this.studentId = studentId;
        this.studentName = studentName;
        this.present = present;
        this.markedAt = markedAt;
    }
}