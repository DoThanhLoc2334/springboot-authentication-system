package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.response.JwtResponse;
import com.dtl.springboot_auth_system.dto.request.LoginRequest;
import com.dtl.springboot_auth_system.dto.request.RefreshTokenRequest;
import com.dtl.springboot_auth_system.dto.request.RegisterRequest;
import com.dtl.springboot_auth_system.dto.request.ForgotPasswordRequest;
import com.dtl.springboot_auth_system.dto.request.VerifyOtpRequest;
import com.dtl.springboot_auth_system.dto.request.ResetPasswordRequest;
import com.dtl.springboot_auth_system.dto.response.OtpResponse;
import com.dtl.springboot_auth_system.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse jwtResponse = authService.login(request);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        JwtResponse jwtResponse = authService.refreshToken(request);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> logout() {
        authService.logoutCurrentUser();
        return ResponseEntity.ok("Logout successful. Remove tokens on the client side.");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<OtpResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(new OtpResponse("OTP has been sent to your email. Please check your inbox."));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<OtpResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.ok(new OtpResponse("OTP verified successfully."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<OtpResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(new OtpResponse("Password reset successfully. You can now log in with your new password."));
    }
}
