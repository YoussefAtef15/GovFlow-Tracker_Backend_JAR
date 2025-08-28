package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PayableServiceDto {
    private Long id;
    private String name;
    private BigDecimal amount;
}