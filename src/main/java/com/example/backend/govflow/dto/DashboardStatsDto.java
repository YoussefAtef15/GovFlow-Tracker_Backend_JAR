package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatsDto {
    private String label;
    private int count;
    private String icon;
    private String colorClass;
}