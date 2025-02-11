package com.example.springboot.entities.user;

public record RegisterDTO(
        String login,
        String password,
        UserRole role
) {

}
