package com.example.springboot.models.user;

public record RegisterDTO(
        String login,
        String password,
        UserRole role
) {

}
