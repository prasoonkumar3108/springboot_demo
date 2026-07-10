package com.company.employeemanagement.controller;

import com.company.employeemanagement.dto.request.LoginRequest;
import com.company.employeemanagement.dto.request.RegisterRequest;
import com.company.employeemanagement.dto.response.ApiResponse;
import com.company.employeemanagement.dto.response.JwtResponse;
import com.company.employeemanagement.dto.response.UserResponse;
import com.company.employeemanagement.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse user = userService.register(request);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(user)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwtResponse = userService.login(request);
        ApiResponse<JwtResponse> response = ApiResponse.<JwtResponse>builder()
                .success(true)
                .message("Login successful")
                .data(jwtResponse)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }
@GetMapping("/profile")
public ResponseEntity<ApiResponse<UserResponse>> profile(Authentication authentication) {

    UserResponse user = userService.getCurrentUser(authentication.getName());

    return ResponseEntity.ok(
            ApiResponse.<UserResponse>builder()
                    .success(true)
                    .message("Profile fetched")
                    .data(user)
                    .timestamp(LocalDateTime.now())
                    .build()
    );
}
}
