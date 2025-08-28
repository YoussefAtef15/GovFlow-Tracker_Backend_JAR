package com.example.backend.govflow.dto;

import com.example.backend.govflow.entity.RequestStatus;
import lombok.Data;

@Data
public class UpdateRequestStatusDto {
    private RequestStatus status;
}