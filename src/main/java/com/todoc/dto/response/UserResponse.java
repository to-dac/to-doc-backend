package com.todoc.dto.response;

import com.todoc.domain.User;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String userName,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getUserName(), user.getCreatedAt());
    }
}
