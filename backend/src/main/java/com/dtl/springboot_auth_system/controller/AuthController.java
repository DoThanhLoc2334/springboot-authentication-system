package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.dto.LoginRequest;
import com.dtl.springboot_auth_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dtl.springboot_auth_system.dto.JwtResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Sử dụng Lombok để tự tạo Constructor cho các field 'final'
public class AuthController {

    // Sử dụng 'final' và Constructor Injection thay vì @Autowired trên field
    // Đây là cách làm chuẩn để dễ viết Unit Test và hạn chế lỗi null
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Đăng ký tài khoản thành công!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // BƯỚC QUAN TRỌNG: Bạn phải khai báo biến 'token' ở đây
        String token = authService.login(request);

        // Sau đó mới truyền biến 'token' vào JwtResponse
        return ResponseEntity.ok(new JwtResponse(token, "Bearer"));
    }
}