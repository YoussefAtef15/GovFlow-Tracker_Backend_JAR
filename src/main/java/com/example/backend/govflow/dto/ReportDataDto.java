package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ReportDataDto {
    private ReportStatsDto stats;
    private List<PerformanceMetricDto> performanceMetrics;
}