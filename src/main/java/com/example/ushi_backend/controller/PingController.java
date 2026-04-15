package com.example.ushi_backend.controller;

import com.example.ushi_backend.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ping")
public class PingController {
    @PostMapping()
    public ResponseEntity<ApiResponse> requestRegisterOtp() {
        ApiResponse response = new ApiResponse("ok");
        return ResponseEntity.ok(response);
    }
}
