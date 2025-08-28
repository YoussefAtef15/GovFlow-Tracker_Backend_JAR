package com.example.backend.govflow.repository;

import com.example.backend.govflow.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    List<Service> findByDepartmentName(String departmentName);

    /**
     * Finds a service by its unique name.
     * @param name The name of the service.
     * @return An Optional containing the service if found.
     */
    Optional<Service> findByName(String name);
}