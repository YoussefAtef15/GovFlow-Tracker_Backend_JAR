package com.example.backend.govflow.controller;

import com.example.backend.govflow.dto.DashboardStatsDto;
import com.example.backend.govflow.dto.ServiceRequestDto;
import com.example.backend.govflow.dto.UpdateRequestStatusDto;
import com.example.backend.govflow.entity.User;
import com.example.backend.govflow.service.ServiceRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('EMPLOYEE') or hasAuthority('MANAGER')")
public class EmployeeController {

    private final ServiceRequestService serviceRequestService;

    @GetMapping("/requests")
    public ResponseEntity<List<ServiceRequestDto>> getAllRequestsForDepartment(@AuthenticationPrincipal User employee) {
        String departmentName = employee.getDepartment().getName();
        List<ServiceRequestDto> requests = serviceRequestService.getAllRequestsForDepartment(departmentName);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<DashboardStatsDto>> getDashboardStats(@AuthenticationPrincipal User employee) {
        String departmentName = employee.getDepartment().getName();
        List<DashboardStatsDto> stats = serviceRequestService.getDepartmentStats(departmentName);
        return ResponseEntity.ok(stats);
    }

    @PatchMapping("/requests/{requestId}/status")
    public ResponseEntity<ServiceRequestDto> updateRequestStatus(
            @PathVariable Long requestId,
            @RequestBody UpdateRequestStatusDto statusDto) {
        ServiceRequestDto updatedRequest = serviceRequestService.updateRequestStatus(requestId, statusDto.getStatus());
        return ResponseEntity.ok(updatedRequest);
    }
}