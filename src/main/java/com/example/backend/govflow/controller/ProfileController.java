//package com.example.backend.govflow.controller;
//
//import com.example.backend.govflow.dto.ChangePasswordDto;
//import com.example.backend.govflow.dto.UpdateProfileDto;
//import com.example.backend.govflow.dto.UserDto;
//import com.example.backend.govflow.entity.User;
//import com.example.backend.govflow.service.UserService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/profile")
//@RequiredArgsConstructor
//public class ProfileController {
//
//    private final UserService userService;
//
//    @GetMapping
//    public ResponseEntity<UserDto> getMyProfile(@AuthenticationPrincipal User currentUser) {
//        UserDto userDto = new UserDto(
//                currentUser.getId(),
//                currentUser.getFullName(),
//                currentUser.getEmail(),
//                currentUser.getRole().getName()
//        );
//        return ResponseEntity.ok(userDto);
//    }
//
//    @PutMapping
//    public ResponseEntity<UserDto> updateMyProfile(
//            @AuthenticationPrincipal User currentUser,
//            @Valid @RequestBody UpdateProfileDto updateProfileDto) {
//
//        UserDto updatedUser = userService.updateProfile(currentUser.getId(), updateProfileDto);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    @PostMapping("/change-password")
//    public ResponseEntity<Void> changeMyPassword(
//            @AuthenticationPrincipal User currentUser,
//            @Valid @RequestBody ChangePasswordDto changePasswordDto) {
//
//        userService.changePassword(currentUser.getId(), changePasswordDto);
//        return ResponseEntity.ok().build();
//    }
//}


package com.example.backend.govflow.controller;

import com.example.backend.govflow.dto.ChangePasswordDto;
import com.example.backend.govflow.dto.UpdateProfileDto;
import com.example.backend.govflow.dto.UserDto;
import com.example.backend.govflow.entity.User;
import com.example.backend.govflow.service.FileStorageService;
import com.example.backend.govflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // استيراد HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId, @AuthenticationPrincipal User currentUser) {
        // ✅ START: إضافة تحقق أمني
        if (!currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // ✅ END: نهاية التحقق
        User user = userService.findUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateMyProfile(
            @PathVariable Long userId,
            @RequestParam("profileData") String profileDataJson,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
            @AuthenticationPrincipal User currentUser) throws IOException {

        // ✅ START: إضافة تحقق أمني
        if (!currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // ✅ END: نهاية التحقق

        UpdateProfileDto updateProfileDto = objectMapper.readValue(profileDataJson, UpdateProfileDto.class);
        UserDto updatedUser = userService.updateProfile(userId, updateProfileDto, avatarFile);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Void> changeMyPassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordDto changePasswordDto,
            @AuthenticationPrincipal User currentUser) {

        // ✅ START: إضافة تحقق أمني
        if (!currentUser.getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // ✅ END: نهاية التحقق

        userService.changePassword(userId, changePasswordDto);
        return ResponseEntity.ok().build();
    }
}