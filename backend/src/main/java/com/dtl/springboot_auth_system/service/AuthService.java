package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.dto.LoginRequest;

public interface AuthService {
    void register(RegisterRequest request);

    String login(LoginRequest request); 
}