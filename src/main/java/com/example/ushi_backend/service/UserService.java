package com.example.ushi_backend.service;

import com.example.ushi_backend.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();

    UserResponse getByEmail(String email);
}
