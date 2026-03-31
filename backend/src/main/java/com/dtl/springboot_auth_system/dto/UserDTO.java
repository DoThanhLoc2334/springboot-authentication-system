package com.dtl.springboot_auth_system.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private boolean enabled;
    private Set<String> roles;
}