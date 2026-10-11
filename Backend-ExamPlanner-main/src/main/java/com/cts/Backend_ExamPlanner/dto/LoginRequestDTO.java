package com.cts.Backend_ExamPlanner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class LoginRequestDTO {
    @NotEmpty(message = "Email can not be null or empty")
    @Email(message = "Email is required")
    private String email;
    @NotEmpty(message = "Password is required")
    @Size(min = 8, message = "Password must be atleast 8 characters")
    private String password;
    @NotEmpty(message = "User role is required")
    private String role;
}
