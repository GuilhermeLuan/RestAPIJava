package com.example.springboot.services;

import com.example.springboot.entities.user.UserModel;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.utils.UserCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class AuthorizationServiceTest {
    @InjectMocks
    private AuthorizationService authorizationService;
    @Mock
    private UserRepository userRepositoryMock;

    @Test
    void loadByUsername_ShouldReturnsAUser_WhenSuccessful() {
        UserModel userToBeSaved = UserCreator.createAdminToBeSaved();

        BDDMockito.when(userRepositoryMock.findByLogin(ArgumentMatchers.anyString()))
                .thenReturn(userToBeSaved);

        var user = authorizationService.loadUserByUsername("user");

        Assertions.assertThat(user).isNotNull().isEqualTo(userToBeSaved);
    }

    @Test
    void loadByUsername_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        BDDMockito.when(userRepositoryMock.findByLogin(ArgumentMatchers.anyString()))
                .thenThrow(new UsernameNotFoundException("User not found"));

        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> authorizationService.loadUserByUsername("user"));
    }
}