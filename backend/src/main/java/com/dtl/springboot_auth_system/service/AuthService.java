package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.request.RegisterRequest;
import com.dtl.springboot_auth_system.dto.request.LoginRequest;
import com.dtl.springboot_auth_system.dto.request.RefreshTokenRequest;
import com.dtl.springboot_auth_system.dto.request.ForgotPasswordRequest;
import com.dtl.springboot_auth_system.dto.request.VerifyOtpRequest;
import com.dtl.springboot_auth_system.dto.request.ResetPasswordRequest;
import com.dtl.springboot_auth_system.dto.response.JwtResponse;
public interface AuthService {
    void register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    JwtResponse refreshToken(RefreshTokenRequest request);

    void logoutCurrentUser();

    void forgotPassword(ForgotPasswordRequest request);

    void verifyOtp(VerifyOtpRequest request);

    void resetPassword(ResetPasswordRequest request);
}
