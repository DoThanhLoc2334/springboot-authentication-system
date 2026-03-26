package com.dtl.springboot_auth_system.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;

    private String deviceInfo; // "Chrome on Windows", "Mobile Firefox"...

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getDeviceInfo() { 
        return deviceInfo != null ? deviceInfo : "Unknown Device"; 
    }
}