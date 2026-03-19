package com.dtl.springboot_auth_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/register")
    public String showRegisterPage() {
        return "auth/register";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        // Trỏ đúng vào thư mục user/ đã tạo
        return "user/profile";
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard() {
        // Trỏ vào thư mục admin/ file admin-dashboard.html
        return "admin/admin-dashboard";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "auth/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage() {
        return "auth/reset-password";
    }
}
