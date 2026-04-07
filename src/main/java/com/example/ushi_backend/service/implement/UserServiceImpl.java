package com.example.ushi_backend.service.implement;

import com.example.ushi_backend.dto.response.UserResponse;
import com.example.ushi_backend.entity.UserEntity;
import com.example.ushi_backend.exception.ResourceNotFoundException;
import com.example.ushi_backend.mapper.UserMapper;
import com.example.ushi_backend.repository.UserRepository;
import com.example.ushi_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    @Override
    public UserResponse getByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay nguoi dung"));
        return userMapper.toUserResponse(user);
    }
}
