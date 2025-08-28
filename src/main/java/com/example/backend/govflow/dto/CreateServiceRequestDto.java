package com.example.backend.govflow.dto;

import lombok.Data;

@Data
public class CreateServiceRequestDto {
    private String serviceName;
    private String details;
    private String department;
}