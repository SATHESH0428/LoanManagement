package com.loanmanagement.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.loanmanagement.dao.AuthDao;
import com.loanmanagement.service.AuthService;

class AuthServiceTest {

    private AuthService authService;
    private AuthDao authDao;

    @BeforeEach
    void setup() throws Exception {

        authService = new AuthService();
        authDao = mock(AuthDao.class);

    
        Field field = AuthService.class.getDeclaredField("authDao");
        field.setAccessible(true);
        field.set(authService, authDao);
    }


    @Test
    void testLoginSuccess() {

        when(authDao.validateLogin(anyString(), anyString()))
                .thenReturn(1L);

        String token = authService.login("user", "pass");

        assertNotNull(token);
        verify(authDao).validateLogin(anyString(), anyString());
        verify(authDao).saveToken(eq(1L), anyString());
    }

 
    @Test
    void testLoginInvalidCredentials() {

        when(authDao.validateLogin(anyString(), anyString()))
                .thenReturn(0L);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authService.login("bad", "bad"));

        assertEquals("Invalid username or password", ex.getMessage());
    }

  
    @Test
    void testLoginNullInput() {

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authService.login(null, null));

        assertEquals("Username and password required", ex.getMessage());
    }

 
    @Test
    void testValidateTokenSuccess() {

        when(authDao.isTokenValid("token123")).thenReturn(true);

        boolean result = authService.validateToken("token123");

        assertTrue(result);
        verify(authDao).isTokenValid("token123");
    }

 
    @Test
    void testValidateTokenEmpty() {

        boolean result = authService.validateToken("");

        assertFalse(result);
        verify(authDao, never()).isTokenValid(anyString());
    }
}
