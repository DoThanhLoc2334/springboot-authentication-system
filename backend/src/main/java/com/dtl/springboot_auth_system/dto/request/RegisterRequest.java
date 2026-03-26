package com.dtl.springboot_auth_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 50, message = "Username phải từ 3-50 ký tự")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, max = 120, message = "Password phải từ 6-120 ký tự")
    private String password;

    private String role; 

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}