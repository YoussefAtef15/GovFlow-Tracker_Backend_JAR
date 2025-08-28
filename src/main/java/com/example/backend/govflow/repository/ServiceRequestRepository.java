package com.example.backend.govflow.repository;

import com.example.backend.govflow.entity.RequestStatus;
import com.example.backend.govflow.entity.ServiceRequest;
import com.example.backend.govflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    // --- الدوال الحالية الخاصة بك ---
    long countByCitizenAndStatus(User citizen, RequestStatus status);

    List<ServiceRequest> findByCitizenId(Long citizenId);

    List<ServiceRequest> findTop5ByCitizenOrderBySubmissionDateDesc(User citizen);

    // ✅ ===== الجزء الذي تمت إضافته لدعم EmployeeController =====

    /**
     * Finds all service requests belonging to a specific department.
     * @param department The name of the department.
     * @return A list of service requests.
     */
    List<ServiceRequest> findByDepartment(String department);

    /**
     * Counts all service requests in a specific department with a given status.
     * @param department The name of the department.
     * @param status The status of the request.
     * @return The count of matching service requests.
     */
    long countByDepartmentAndStatus(String department, RequestStatus status);

    /**
     * Finds service requests in a specific department with a given status.
     * (هذه الدالة كانت موجودة بالفعل ولكن للتأكيد)
     * @param department The name of the department.
     * @param status The status of the request.
     * @return A list of matching service requests.
     */
    List<ServiceRequest> findByDepartmentAndStatus(String department, RequestStatus status);

    List<ServiceRequest> findByCitizenIdAndStatus(Long citizenId, RequestStatus status);
    // ==================================================================
    // ✅ ===== START: الدالة الجديدة التي تمت إضافتها للتقارير ===== ✅
    // ==================================================================

    /**
     * Counts the total number of service requests for a specific department.
     * @param department The name of the department.
     * @return The total count of requests.
     */
    long countByDepartment(String department);
    // ==================================================================
    // ✅ ===== END: الدالة الجديدة التي تمت إضافتها ===== ✅
    // ==================================================================
    List<ServiceRequest> findByCitizenIdAndStatusAndFeeIsNotNull(Long citizenId, RequestStatus status);

}