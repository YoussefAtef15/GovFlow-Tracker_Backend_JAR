package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportStatsDto {
    private String totalRequests;
    private String approved;
    private String rejected;
    private String avgProcessingTime;
}