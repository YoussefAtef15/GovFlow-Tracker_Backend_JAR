package com.example.backend.govflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateEmployeeDto {

    @NotEmpty(message = "Full name cannot be empty.")
    private String fullName;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    @NotNull(message = "Role ID is required.")
    private Long roleId;

    @NotNull(message = "Department ID is required.")
    private Long departmentId;
}