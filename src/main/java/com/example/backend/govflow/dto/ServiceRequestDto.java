package com.example.backend.govflow.dto;

import com.example.backend.govflow.entity.RequestStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ServiceRequestDto {
    private Long id;
    private String serviceName;
    private RequestStatus status;
    private LocalDateTime submissionDate;
    private String details;
    private String department; // <-- هذا السطر ناقص عندك، قم بإضافته
}