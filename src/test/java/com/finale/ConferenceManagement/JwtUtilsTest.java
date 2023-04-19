package com.finale.ConferenceManagement;

import com.finale.ConferenceManagement.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
    }

    @Test
    void generateToken() {
        String username = "testUser";
        String token = jwtUtils.generateToken(username);
        assertNotNull(token);
    }

    @Test
    void getUsernameFromToken() {
        String username = "testUser";
        String token = jwtUtils.generateToken(username);

        String extractedUsername = jwtUtils.getUsernameFromToken(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken() {
        String username = "testUser";
        String token = jwtUtils.generateToken(username);

        try {
            Thread.sleep(1000); // wait for the token to expire
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        boolean isValid = jwtUtils.validateToken(token);
        assertFalse(isValid);
    }
}