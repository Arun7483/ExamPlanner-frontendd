package com.cts.Backend_ExamPlanner.dto;

import com.cts.Backend_ExamPlanner.entities.Availability;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HallDTO {
    @NotBlank(message = "Hall ID is required")
    private String hallId;
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer Capacity;
    @NotBlank(message = "Floor description is required")
    private String floor;
    @NotNull(message = "Availability status is required")
    private Availability availability;
}
