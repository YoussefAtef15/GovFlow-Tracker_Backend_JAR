package com.example.backend.govflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateProfileDto {
    @NotEmpty(message = "Full name cannot be empty.")
    private String fullName;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    private String phoneNumber;
    private String address;
}