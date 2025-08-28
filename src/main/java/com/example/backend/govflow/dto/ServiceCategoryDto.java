package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class ServiceCategoryDto {
    private String id;
    private String name;
    private String description;
    private String icon;
    private List<String> tags;
}