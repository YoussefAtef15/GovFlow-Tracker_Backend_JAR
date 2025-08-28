package com.example.backend.govflow.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String nationalId;
    private String password;
}
