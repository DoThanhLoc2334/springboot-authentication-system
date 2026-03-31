package com.dtl.springboot_auth_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username or email must not be blank")
    private String usernameOrEmail;

    @NotBlank(message = "Password must not be blank")
    private String password;
}