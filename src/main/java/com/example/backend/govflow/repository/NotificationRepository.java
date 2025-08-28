package com.example.backend.govflow.repository;

import com.example.backend.govflow.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // دالة لجلب كل إشعارات مستخدم معين، مرتبة من الأحدث للأقدم
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}