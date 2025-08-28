package com.example.backend.govflow.controller;

import com.example.backend.govflow.dto.ReportDataDto; // ✅ 1. استيراد إضافي
import com.example.backend.govflow.dto.UpdateEmployeeDto; // ✅ 2. استيراد إضافي
import com.example.backend.govflow.dto.UserDto;
import com.example.backend.govflow.service.ServiceRequestService; // ✅ 3. استيراد إضافي
import com.example.backend.govflow.service.UserService;
import jakarta.validation.Valid; // ✅ 4. استيراد إضافي
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('MANAGER')") // <-- فقط المدير يمكنه الوصول لهذه الوظائف
public class ManagerController {

    private final UserService userService;
    private final ServiceRequestService serviceRequestService; // ✅ 5. حقن الخدمة الجديدة

    @GetMapping("/employees")
    public ResponseEntity<List<UserDto>> getAllEmployees() {
        List<UserDto> employees = userService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ==================================================================
    // ✅ ===== START: الدوال الجديدة التي تمت إضافتها ===== ✅
    // ==================================================================

    /**
     * Endpoint for updating an employee's details.
     * @param id The ID of the employee to update.
     * @param employeeDto The new details for the employee.
     * @return The updated employee data.
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<UserDto> updateEmployee(@PathVariable Long id, @Valid @RequestBody UpdateEmployeeDto employeeDto) {
        UserDto updatedUser = userService.updateEmployee(id, employeeDto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Endpoint for fetching aggregated report data.
     * @return A DTO containing stats and performance metrics.
     */
    @GetMapping("/reports")
    public ResponseEntity<ReportDataDto> getReportData() {
        ReportDataDto reportData = serviceRequestService.getReportData();
        return ResponseEntity.ok(reportData);
    }
    // ==================================================================
    // ✅ ===== END: الدوال الجديدة التي تمت إضافتها ===== ✅
    // ==================================================================
}