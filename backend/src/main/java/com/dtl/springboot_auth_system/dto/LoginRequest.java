package com.dtl.springboot_auth_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Username khong duoc de trong")
    private String username;

    @NotBlank(message = "Mat khau khong duoc de trong")
    private String password;
}
