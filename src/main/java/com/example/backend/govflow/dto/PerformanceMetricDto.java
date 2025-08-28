package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerformanceMetricDto {
    private String department;
    private long totalRequests;
    private long approved;
    private long rejected;
    private String approvalRate;
    private double avgTime; // In days
    private String slaCompliance;
}