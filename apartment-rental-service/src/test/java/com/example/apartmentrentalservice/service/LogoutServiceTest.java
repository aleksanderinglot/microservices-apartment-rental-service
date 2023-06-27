package com.example.apartmentrentalservice.service;

import com.example.apartmentrentalservice.model.Token;
import com.example.apartmentrentalservice.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LogoutServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private LogoutService logoutService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logoutService = new LogoutService(tokenRepository);
    }

    @Test
    void shouldLogoutWhenTokenIsValid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer testToken");
        Token mockedToken = Mockito.mock(Token.class);
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(mockedToken));

        logoutService.logout(request, response, null);

        verify(tokenRepository, times(1)).findByToken(anyString());
        verify(tokenRepository, times(1)).save(any(Token.class));
        verify(response, never()).setStatus(anyInt());
        verify(response, never()).sendError(anyInt());
        verify(response, never()).getWriter();
    }

    @Test
    void shouldNotLogoutWhenTokenIsInvalid() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        logoutService.logout(request, response, null);

        verify(tokenRepository, never()).findByToken(anyString());
        verify(tokenRepository, never()).save(any(Token.class));
        verify(response, never()).setStatus(anyInt());
        verify(response, never()).sendError(anyInt());
        verify(response, never()).getWriter();
    }
}
