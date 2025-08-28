package com.example.backend.govflow.controller;

import com.example.backend.govflow.dto.*;
import com.example.backend.govflow.entity.User;
import com.example.backend.govflow.security.jwt.JwtService;
import com.example.backend.govflow.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getNationalId(), request.getPassword())
        );

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user);

        UserDto userDto = new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().getName());

        return ResponseEntity.ok(new AuthResponse(token, userDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user);

        UserDto userDto = new UserDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().getName());

        return ResponseEntity.ok(new AuthResponse(token, userDto));
    }
}