package com.Cataloger.dto.response;

import com.Cataloger.entity.Role;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String login;
    private Role role;
    private boolean active;
}
