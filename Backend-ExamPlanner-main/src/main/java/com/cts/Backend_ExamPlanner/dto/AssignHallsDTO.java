package com.cts.Backend_ExamPlanner.dto;

import com.cts.Backend_ExamPlanner.entities.HallAllocation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AssignHallsDTO {
    @NotNull(message = "Exam ID is required")
    private Long examId;
    @NotEmpty(message = "At least one hall assignment is required")
    @Valid // CRITICAL: This triggers validation on the objects inside the list
    private List<HallInvigilatorPair> assignments;

    @Data
    public static class HallInvigilatorPair {
//        @NotBlank(message = "Hall ID is required")
        private String hallId;
//        @NotBlank(message = "Invigilator ID is required")
        private String invigilatorId;
    }
}
