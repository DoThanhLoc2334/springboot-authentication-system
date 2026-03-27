package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.dto.LoginRequest;
import com.dtl.springboot_auth_system.dto.JwtResponse;
public interface AuthService {
    void register(RegisterRequest request);

    JwtResponse login(LoginRequest request); 
}