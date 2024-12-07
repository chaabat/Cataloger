package com.Cataloger.serviceTest;

 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Cataloger.controller.AuthController;
import com.Cataloger.dto.request.LoginRequest;
import com.Cataloger.dto.request.UserRequest;
import com.Cataloger.dto.response.UserResponse;
import com.Cataloger.entity.Role;
import com.Cataloger.service.interfaces.UserService;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private UserResponse mockUserResponse;
    private UserRequest mockUserRequest;
    private LoginRequest mockLoginRequest;
    private Authentication mockAuthentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock user response
        mockUserResponse = new UserResponse();
        mockUserResponse.setId(1L);
        mockUserResponse.setLogin("testUser");
        mockUserResponse.setRole(Role.ROLE_USER);
        mockUserResponse.setActive(true);

        // Setup mock user request
        mockUserRequest = new UserRequest();
        mockUserRequest.setLogin("testUser");
        mockUserRequest.setPassword("password123");

        // Setup mock login request
        mockLoginRequest = new LoginRequest();
        mockLoginRequest.setLogin("testUser");
        mockLoginRequest.setPassword("password123");

        // Setup mock authentication
        mockAuthentication = new UsernamePasswordAuthenticationToken(
            mockLoginRequest.getLogin(), 
            mockLoginRequest.getPassword()
        );
    }

    @Test
    void createUser_Success() {
        when(userService.createUser(any(UserRequest.class))).thenReturn(mockUserResponse);

         ResponseEntity<UserResponse> response = authController.createUser(mockUserRequest);

         assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockUserResponse, response.getBody());
        verify(userService, times(1)).createUser(mockUserRequest);
    }

    @Test
    void login_Success() {
        when(authenticationManager.authenticate(any(Authentication.class)))
            .thenReturn(mockAuthentication);
        when(userService.findByLogin(mockLoginRequest.getLogin()))
            .thenReturn(mockUserResponse);

         ResponseEntity<UserResponse> response = authController.login(mockLoginRequest);

         assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserResponse, response.getBody());
        verify(authenticationManager, times(1))
            .authenticate(any(Authentication.class));
        verify(userService, times(1))
            .findByLogin(mockLoginRequest.getLogin());
    }

    @Test
    void login_InvalidCredentials() {
        when(authenticationManager.authenticate(any(Authentication.class)))
            .thenThrow(new RuntimeException("Invalid credentials"));

        
        assertThrows(RuntimeException.class, () -> {
            authController.login(mockLoginRequest);
        });
        verify(authenticationManager, times(1))
            .authenticate(any(Authentication.class));
        verify(userService, never()).findByLogin(any());
    }

    @Test
    void createUser_WithExistingUsername() {
      
        when(userService.createUser(any(UserRequest.class)))
            .thenThrow(new RuntimeException("Username already exists"));

       
        assertThrows(RuntimeException.class, () -> {
            authController.createUser(mockUserRequest);
        });
        verify(userService, times(1)).createUser(mockUserRequest);
    }



    @Test
    void login_WithNullRequest() {
        
        assertThrows(NullPointerException.class, () -> {
            authController.login(null);
        });
        verify(authenticationManager, never()).authenticate(any());
        verify(userService, never()).findByLogin(any());
    }
}