package com.example.springboot.controllers;

import com.example.springboot.entities.user.AuthenticationDTO;
import com.example.springboot.entities.user.LoginResponseDTO;
import com.example.springboot.entities.user.UserModel;
import com.example.springboot.infra.security.TokenService;
import com.example.springboot.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {

    @InjectMocks
    private AuthenticationController authenticationController;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenService tokenService;

    @Test
    void login_ShouldReturnJWTToken_WhenSuccessful() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("login", "password");
        UserModel usuarioMock = new UserModel();
        usuarioMock.setLogin("login");
        Authentication authenticationMock = new UsernamePasswordAuthenticationToken(usuarioMock, null);

        BDDMockito.when(authenticationManager.authenticate(BDDMockito.any()))
                .thenReturn(authenticationMock);
        String token = "token-de-teste";

        BDDMockito.when(tokenService.generateToken(BDDMockito.any()))
                .thenReturn(token);

        ResponseEntity<LoginResponseDTO> responseEntity = authenticationController.login(authenticationDTO);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(responseEntity.getBody()).token()).isNotNull().isEqualTo(token);
    }
}