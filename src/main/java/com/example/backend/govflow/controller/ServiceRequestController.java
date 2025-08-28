package com.example.backend.govflow.controller;

import com.example.backend.govflow.dto.CreateServiceRequestDto;
import com.example.backend.govflow.dto.ServiceRequestDto;
import com.example.backend.govflow.entity.User;
import com.example.backend.govflow.service.ServiceRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;
    private final ObjectMapper objectMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CITIZEN')")
    public ResponseEntity<ServiceRequestDto> createServiceRequest(
            @RequestParam("request") String requestJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            CreateServiceRequestDto requestDto = objectMapper.readValue(requestJson, CreateServiceRequestDto.class);

            ServiceRequestDto newRequest = serviceRequestService.createRequest(requestDto, currentUser, files);
            return new ResponseEntity<>(newRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CITIZEN')")
    public ResponseEntity<List<ServiceRequestDto>> getMyRequests(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<ServiceRequestDto> requests = serviceRequestService.getRequestsForCitizen(currentUser.getId());
        return ResponseEntity.ok(requests);
    }
}