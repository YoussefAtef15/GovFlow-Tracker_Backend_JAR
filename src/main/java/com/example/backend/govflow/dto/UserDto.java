package com.example.backend.govflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String role;

    // --- الإضافة المطلوبة لدعم صورة البروفايل ---
    private String avatar;

    /**
     * Constructor إضافي للحفاظ على التوافق مع الأجزاء القديمة من الكود
     * التي لا تتعامل مع الصورة.
     */
    public UserDto(Long id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }
}