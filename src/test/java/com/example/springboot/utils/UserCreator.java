package com.example.springboot.utils;

import com.example.springboot.entities.user.UserModel;
import com.example.springboot.entities.user.UserRole;

public class UserCreator {
    public static UserModel createUserToBeSaved() {
        return UserModel.builder()
                .login("USER")
                .password("USER")
                .role(UserRole.USER)
                .build();
    }

    public static UserModel createAdminToBeSaved() {
        return UserModel.builder()
                .login("ADMIN")
                .password("ADMIN")
                .role(UserRole.ADMIN)
                .build();
    }
}
