package com.example.backend.govflow.repository;

import com.example.backend.govflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // ✅ 1. استيراد إضافي
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNationalId(String nationalId);

    boolean existsByEmail(String email);

    boolean existsByNationalId(String nationalId);

    // ✅ ===== START: الدالة الجديدة التي تمت إضافتها ===== ✅
    /**
     * Finds all users whose role name is not the one provided.
     * This is used to fetch all employees and managers, excluding citizens.
     * @param roleName The name of the role to exclude (e.g., "CITIZEN").
     * @return A list of users.
     */
    List<User> findAllByRoleNameNot(String roleName);
    // ✅ ===== END: الدالة الجديدة التي تمت إضافتها ===== ✅
}