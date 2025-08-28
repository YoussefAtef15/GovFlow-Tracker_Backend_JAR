package com.example.backend.govflow.controller;

import com.example.backend.govflow.dto.*;
import com.example.backend.govflow.entity.Department;
import com.example.backend.govflow.entity.ServiceRequest;
import com.example.backend.govflow.entity.User;
import com.example.backend.govflow.repository.DepartmentRepository;
import com.example.backend.govflow.repository.ServiceRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.backend.govflow.entity.RequestStatus.*;

@RestController
@RequestMapping("/api/citizen")
@RequiredArgsConstructor
public class CitizenController {

    private final ServiceRequestRepository serviceRequestRepository;
    private final DepartmentRepository departmentRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<CitizenDashboardDataDto> getDashboardData(@AuthenticationPrincipal User currentUser) {
        String citizenName = currentUser.getFullName();

        long activeCount = serviceRequestRepository.countByCitizenAndStatus(currentUser, IN_PROGRESS);
        long completedCount = serviceRequestRepository.countByCitizenAndStatus(currentUser, COMPLETED);
        long pendingCount = serviceRequestRepository.countByCitizenAndStatus(currentUser, PENDING_CITIZEN_ACTION);

        List<DashboardStatsDto> stats = Arrays.asList(
                new DashboardStatsDto("Active Requests", (int) activeCount, "fas fa-spinner", "bg-blue"),
                new DashboardStatsDto("Completed", (int) completedCount, "fas fa-check-circle", "bg-green"),
                new DashboardStatsDto("Pending Action", (int) pendingCount, "fas fa-exclamation-triangle", "bg-yellow")
        );

        List<ServiceCategoryDto> categories = departmentRepository.findAll()
                .stream()
                .map(this::mapDepartmentToCategoryDto)
                .collect(Collectors.toList());

        List<RequestItemDto> recentRequests = serviceRequestRepository.findTop5ByCitizenOrderBySubmissionDateDesc(currentUser)
                .stream()
                .map(this::mapRequestToItemDto)
                .collect(Collectors.toList());

        CitizenDashboardDataDto dashboardData = new CitizenDashboardDataDto(citizenName, stats, categories, recentRequests);

        return ResponseEntity.ok(dashboardData);
    }

    private ServiceCategoryDto mapDepartmentToCategoryDto(Department department) {
        if (department.getName().equals("Traffic Department")) {
            return new ServiceCategoryDto("traffic", "Traffic Services", "Renew licenses, pay fines, and more.", "fas fa-car", Arrays.asList("License", "Fines", "Vehicles"));
        } else if (department.getName().equals("Local Municipality")) {
            return new ServiceCategoryDto("municipality", "Municipality Services", "Building permits and local services.", "fas fa-city", Arrays.asList("Permits", "Complaints"));
        }
        return new ServiceCategoryDto(department.getName().toLowerCase(), department.getName(), "General services.", "fas fa-concierge-bell", List.of("General"));
    }

    private RequestItemDto mapRequestToItemDto(ServiceRequest request) {
        String statusClass = switch (request.getStatus()) {
            case COMPLETED -> "bg-green";
            case IN_PROGRESS, PENDING, UNDER_REVIEW -> "bg-blue";
            case REJECTED -> "bg-red";
            case PENDING_CITIZEN_ACTION -> "bg-yellow";
            case APPROVED_PENDING_PAYMENT -> "status-under-review"; // Or a new yellow class if you prefer

        };
        return new RequestItemDto(
                "REQ-" + String.format("%03d", request.getId()),
                request.getServiceName(),
                request.getSubmissionDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                request.getStatus().toString(),
                statusClass
        );
    }
}