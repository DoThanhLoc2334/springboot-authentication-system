package com.dtl.springboot_auth_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Dòng quan trọng bị thiếu
import org.springframework.security.core.userdetails.UserDetails; // Dòng quan trọng bị thiếu
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Chúc mừng Huy! Bạn đã truy cập được vào API bảo mật bằng JWT thành công.");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // Spring Security sẽ tự động "nhặt" thông tin từ Token và đổ vào userDetails
        return ResponseEntity
                .ok("Chào " + userDetails.getUsername() + "! Đây là thông tin bảo mật lấy trực tiếp từ JWT của bạn.");
    }
}