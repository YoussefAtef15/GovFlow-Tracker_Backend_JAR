package com.example.backend.govflow.controller;

import com.example.backend.govflow.dto.PayableServiceDto;
import com.example.backend.govflow.dto.PaymentRecordDto;
import com.example.backend.govflow.entity.User;
import com.example.backend.govflow.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/payable")
    public ResponseEntity<List<PayableServiceDto>> getPayableServices(@AuthenticationPrincipal User currentUser) {
        List<PayableServiceDto> services = paymentService.getPayableServicesForUser(currentUser);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/history")
    public ResponseEntity<List<PaymentRecordDto>> getPaymentHistory(@AuthenticationPrincipal User currentUser) {
        List<PaymentRecordDto> history = paymentService.getPaymentHistoryForUser(currentUser);
        return ResponseEntity.ok(history);
    }
}