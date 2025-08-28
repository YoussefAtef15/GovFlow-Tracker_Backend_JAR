package com.example.backend.govflow.repository;

import com.example.backend.govflow.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Finds all payments for a given citizen by looking through their service requests.
     * Orders the results by creation date, with the most recent first.
     */
    List<Payment> findByRequest_Citizen_IdOrderByCreatedAtDesc(Long citizenId);
}