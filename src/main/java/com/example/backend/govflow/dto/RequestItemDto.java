package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestItemDto {
    private String id;
    private String service;
    private String date;
    private String status;
    private String statusClass;
}