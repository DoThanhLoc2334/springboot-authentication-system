package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.dto.LoginRequest;
import com.dtl.springboot_auth_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Sử dụng Lombok để tự tạo Constructor cho các field 'final'
public class AuthController {

    // Sử dụng 'final' và Constructor Injection thay vì @Autowired trên field
    // Đây là cách làm chuẩn để dễ viết Unit Test và hạn chế lỗi null
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        // Thay vì trả về String, ta dùng ResponseEntity để trả về Status Code chuẩn (ví
        // dụ 200 OK)
        authService.register(request);
        return ResponseEntity.ok("Đăng ký tài khoản thành công!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // Tương tự, thêm @Valid để kiểm tra xem user/pass có để trống không
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }
}