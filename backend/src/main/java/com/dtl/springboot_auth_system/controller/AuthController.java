package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.request.LoginRequest;
import com.dtl.springboot_auth_system.dto.request.RegisterRequest;
import com.dtl.springboot_auth_system.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Đăng ký thành công");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok("accessToken: " + newAccessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok("Đăng xuất thành công");
    }

    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        authService.logoutAll(username);
        return ResponseEntity.ok("Đã đăng xuất tất cả thiết bị");
    }
}