package com.example.backend.govflow.service;

import com.example.backend.govflow.dto.*;
import com.example.backend.govflow.entity.*;
import com.example.backend.govflow.repository.DocumentRepository;
import com.example.backend.govflow.repository.ServiceRepository;
import com.example.backend.govflow.repository.ServiceRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat; // ✅ 1. إضافة import
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceRequestService {

    private final ServiceRequestRepository requestRepository;
    private final ServiceRepository serviceRepository;
    private final DocumentRepository documentRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public ServiceRequestDto createRequest(CreateServiceRequestDto dto, User citizen, List<MultipartFile> files) {
        com.example.backend.govflow.entity.Service service = serviceRepository.findByName(dto.getServiceName())
                .orElseThrow(() -> new EntityNotFoundException("Service not found with name: " + dto.getServiceName()));

        ServiceRequest request = ServiceRequest.builder()
                .serviceName(dto.getServiceName())
                .details(dto.getDetails())
                .department(dto.getDepartment())
                .status(RequestStatus.UNDER_REVIEW)
                .citizen(citizen)
                .service(service)
                .build();
        ServiceRequest savedRequest = requestRepository.save(request);

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String filePath = fileStorageService.storeFile(file, savedRequest.getId());
                Document document = new Document();
                document.setRequest(savedRequest);
                document.setFilePath(filePath);
                document.setDocumentType(file.getContentType());
                documentRepository.save(document);
            }
        }
        return mapToDto(savedRequest);
    }

    public List<ServiceRequestDto> getAllRequestsForDepartment(String department) {
        return requestRepository.findByDepartment(department)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<DashboardStatsDto> getDepartmentStats(String department) {
        long pendingCount = requestRepository.countByDepartmentAndStatus(department, RequestStatus.PENDING);
        long underReviewCount = requestRepository.countByDepartmentAndStatus(department, RequestStatus.UNDER_REVIEW);
        long approvedCount = requestRepository.countByDepartmentAndStatus(department, RequestStatus.COMPLETED);
        long rejectedCount = requestRepository.countByDepartmentAndStatus(department, RequestStatus.REJECTED);

        return Arrays.asList(
                new DashboardStatsDto("Pending", (int) pendingCount, "fas fa-clock", "pending"),
                new DashboardStatsDto("Under Review", (int) underReviewCount, "fas fa-eye", "review"),
                new DashboardStatsDto("Approved", (int) approvedCount, "fas fa-check", "approved"),
                new DashboardStatsDto("Rejected", (int) rejectedCount, "fas fa-times", "rejected")
        );
    }

    public List<ServiceRequestDto> getRequestsForCitizen(Long citizenId) {
        return requestRepository.findByCitizenId(citizenId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ServiceRequestDto> getPendingRequestsForDepartment(String department) {
        return requestRepository.findByDepartmentAndStatus(department, RequestStatus.PENDING)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ServiceRequestDto updateRequestStatus(Long requestId, RequestStatus newStatus) {
        ServiceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found with id: " + requestId));
        request.setStatus(newStatus);
        ServiceRequest updatedRequest = requestRepository.save(request);
        return mapToDto(updatedRequest);
    }

    // ==================================================================
    // ✅ ===== START: الدالة الجديدة التي تمت إضافتها للتقارير ===== ✅
    // ==================================================================
    public ReportDataDto getReportData() {
        List<ServiceRequest> allRequests = requestRepository.findAll();
        List<String> departments = allRequests.stream()
                .map(ServiceRequest::getDepartment)
                .distinct()
                .collect(Collectors.toList());

        long total = allRequests.size();
        long approved = allRequests.stream().filter(r -> r.getStatus() == RequestStatus.COMPLETED).count();
        long rejected = allRequests.stream().filter(r -> r.getStatus() == RequestStatus.REJECTED).count();

        DecimalFormat df = new DecimalFormat("#.##");
        ReportStatsDto stats = new ReportStatsDto(
                String.valueOf(total),
                String.valueOf(approved),
                String.valueOf(rejected),
                df.format(3.5) // Average processing time (can be calculated later)
        );

        List<PerformanceMetricDto> metrics = departments.stream().map(dept -> {
            long deptTotal = requestRepository.countByDepartment(dept);
            long deptApproved = requestRepository.countByDepartmentAndStatus(dept, RequestStatus.COMPLETED);
            long deptRejected = requestRepository.countByDepartmentAndStatus(dept, RequestStatus.REJECTED);
            String approvalRate = (deptTotal > 0) ? df.format((double) deptApproved / deptTotal * 100) + "%" : "0%";

            return new PerformanceMetricDto(
                    dept,
                    deptTotal,
                    deptApproved,
                    deptRejected,
                    approvalRate,
                    4.1, // Mock average time
                    "95%" // Mock SLA
            );
        }).collect(Collectors.toList());

        return new ReportDataDto(stats, metrics);
    }
    // ==================================================================
    // ✅ ===== END: الدالة الجديدة التي تمت إضافتها ===== ✅
    // ==================================================================

    private ServiceRequestDto mapToDto(ServiceRequest request) {
        return ServiceRequestDto.builder()
                .id(request.getId())
                .serviceName(request.getServiceName())
                .status(request.getStatus())
                .submissionDate(request.getSubmissionDate())
                .details(request.getDetails())
                .department(request.getDepartment())
                .build();
    }

    // ✅ 2. تم حذف الدالة المكررة والفارغة من هنا
}