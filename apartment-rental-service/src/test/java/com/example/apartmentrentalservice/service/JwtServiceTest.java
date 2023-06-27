package com.example.apartmentrentalservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "635166546A576D5A7134743777217A25432A462D4A614E645267556B58703272");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 86400L);
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String token = generateToken("testUser");

        String username = jwtService.extractUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void shouldGenerateTokenForUser() {
        UserDetails userDetails = createUserDetails("testUser");

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    void shouldGenerateRefreshTokenForUser() {
        UserDetails userDetails = createUserDetails("testUser");

        String refreshToken = jwtService.generateRefreshToken(userDetails);

        assertNotNull(refreshToken);
    }

    @Test
    void shouldBeValidForCorrectUser() {
        String token = generateToken("testUser");
        UserDetails userDetails = createUserDetails("testUser");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void shouldBeInvalidForIncorrectUser() {
        String token = generateToken("testUser");
        UserDetails userDetails = createUserDetails("otherUser");

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertFalse(isValid);
    }

    private String generateToken(String subject) {
        UserDetails userDetails = createUserDetails(subject);
        return jwtService.generateToken(userDetails);
    }

    private UserDetails createUserDetails(String username) {
        return User.withUsername(username)
                .password("password")
                .roles("USER")
                .build();
    }
}
