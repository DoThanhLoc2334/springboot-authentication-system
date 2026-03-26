package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.request.LoginRequest;
import com.dtl.springboot_auth_system.dto.request.RegisterRequest;
import com.dtl.springboot_auth_system.dto.response.AuthResponse;
import com.dtl.springboot_auth_system.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    public void register(RegisterRequest request) {
        userService.registerUser(request);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String accessToken = jwtUtils.generateAccessToken(authentication);

        return tokenService.generateLoginResponse(
                authentication,
                accessToken,
                request.getDeviceInfo()
        );
    }

    public String refreshToken(String refreshToken) {
        return tokenService.refreshAccessToken(refreshToken);
    }

    public void logout(String refreshToken) {
        tokenService.logout(refreshToken);
    }

    public void logoutAll(String username) {
        tokenService.logoutAll(username);
    }
}