package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PaymentRecordDto {
    private Long id;
    private String serviceName;
    private BigDecimal amount;
    private LocalDate date;
    private String status;
}