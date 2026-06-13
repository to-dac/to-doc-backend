package com.todoc.dto.request;

public record CreateUserRequest(
        String email,
        String password,
        String userName
) {}
