package com.example.ushi_backend.controller;

import com.example.ushi_backend.dto.request.LoginRequest;
import com.example.ushi_backend.dto.request.RequestRegisterOtpRequest;
import com.example.ushi_backend.dto.request.RefreshTokenRequest;
import com.example.ushi_backend.dto.request.RegisterRequest;
import com.example.ushi_backend.dto.response.ApiResponse;
import com.example.ushi_backend.dto.response.AuthResponse;
import com.example.ushi_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/request-otp")
    public ResponseEntity<ApiResponse> requestRegisterOtp(@RequestBody RequestRegisterOtpRequest request) {
        return ResponseEntity.ok(authService.requestRegisterOtp(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        ApiResponse apiResponse = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.logout(request));
    }

}
