package com.example.springboot.util;

import com.example.springboot.entities.user.RegisterDTO;

public class UserPostRequestBodyCreator {
    public static RegisterDTO createUserWithRoleAdminPostRequestBody() {
        return new RegisterDTO(
                UserCreator.createAdminToBeSaved().getLogin(),
                UserCreator.createAdminToBeSaved().getPassword(),
                UserCreator.createAdminToBeSaved().getRole()
        );
    }

    public static RegisterDTO createUserWithRoleUserPostRequestBody() {
        return new RegisterDTO(
                UserCreator.createUserToBeSaved().getLogin(),
                UserCreator.createUserToBeSaved().getPassword(),
                UserCreator.createUserToBeSaved().getRole()
        );
    }
}

