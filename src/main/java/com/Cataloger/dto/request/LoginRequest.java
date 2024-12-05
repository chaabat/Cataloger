package com.Cataloger.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Login is mandatory")
    private String login;
    
    @NotBlank(message = "Password is mandatory")
    private String password;
} 