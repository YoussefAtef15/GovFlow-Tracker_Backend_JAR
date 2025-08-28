package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;
    private long timestamp;
}