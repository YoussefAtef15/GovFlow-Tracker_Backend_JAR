package com.example.backend.govflow.controller;

import com.example.backend.govflow.dto.NotificationDto;
import com.example.backend.govflow.entity.User;
import com.example.backend.govflow.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getMyNotifications(@AuthenticationPrincipal User currentUser) {
        List<NotificationDto> notifications = notificationService.getNotificationsForUser(currentUser.getId());
        return ResponseEntity.ok(notifications);
    }
}