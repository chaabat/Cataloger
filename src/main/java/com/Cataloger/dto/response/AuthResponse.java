package com.Cataloger.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private final boolean success;
    private final String message;
    private final UserResponse user;
} 