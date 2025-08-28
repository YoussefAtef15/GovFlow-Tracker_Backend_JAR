package com.example.backend.govflow.controller;

import com.example.backend.govflow.entity.Service;
import com.example.backend.govflow.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/traffic")
@RequiredArgsConstructor // إضافة Lombok
public class TrafficController {

    private final ServiceRepository serviceRepository;

    @GetMapping("/services")
    public ResponseEntity<List<Service>> getTrafficServices() {
        List<Service> services = serviceRepository.findByDepartmentName("Traffic Department");
        return ResponseEntity.ok(services);
    }
}