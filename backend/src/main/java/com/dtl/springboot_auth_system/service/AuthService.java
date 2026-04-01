package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.request.RegisterRequest;
import com.dtl.springboot_auth_system.dto.request.LoginRequest;
import com.dtl.springboot_auth_system.dto.response.JwtResponse;

public interface AuthService {
    void register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    JwtResponse refreshToken(String refreshToken);

    void logout();
}