package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class CitizenDashboardDataDto {
    private String name;
    private List<DashboardStatsDto> stats;
    private List<ServiceCategoryDto> categories;
    private List<RequestItemDto> recentRequests;
}