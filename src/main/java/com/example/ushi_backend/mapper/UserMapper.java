package com.example.ushi_backend.mapper;

import com.example.ushi_backend.dto.response.UserResponse;
import com.example.ushi_backend.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(UserEntity userEntity);
}
