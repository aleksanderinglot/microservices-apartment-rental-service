package com.example.apartmentrentalservice.controller;

import com.example.apartmentrentalservice.dto.AuthResponse;
import com.example.apartmentrentalservice.dto.RegisterRequest;
import com.example.apartmentrentalservice.dto.LoginRequest;
import com.example.apartmentrentalservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterNewUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        AuthResponse expectedResponse = AuthResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .username("username")
                .role("ADMIN")
                .build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> responseEntity = authController.register(registerRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void shouldAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        AuthResponse expectedResponse = AuthResponse.builder()
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .username("username")
                .role("ADMIN")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<AuthResponse> responseEntity = authController.authenticate(loginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void shouldRefreshToken() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authController.refreshToken(request, response);

        verify(authService, times(1)).refreshToken(request, response);
    }
}
