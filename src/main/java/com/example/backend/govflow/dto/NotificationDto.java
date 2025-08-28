package com.example.backend.govflow.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}