package com.Cataloger.dto.response;

import lombok.Data;
import java.util.Set;

import com.Cataloger.entity.Role;

@Data
public class UserResponse {
    private Long id;
    private String login;
    private boolean active;
    private Set<Role> roles;
}
