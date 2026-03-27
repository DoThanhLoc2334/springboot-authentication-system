package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.LoginRequest;
import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.dto.JwtResponse;
import com.dtl.springboot_auth_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Đăng ký thành công!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new JwtResponse(token, "Bearer"));
    }
}