package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class TrafficServiceItemDto {
    private String id;
    private String title;
    private String description;
    private List<String> requiredDocuments;
    private String processingTime;
    private String fee;
    private String icon;
}