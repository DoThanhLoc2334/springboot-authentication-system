package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.response.AuthResponse;
import com.dtl.springboot_auth_system.model.RefreshToken;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenService {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse generateLoginResponse(
            Authentication authentication,
            String accessToken,
            String deviceInfo) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user, deviceInfo);

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                userDetails.getUsername(),
                user.getEmail(),
                roles
        );
    }

    public String refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken);
        refreshTokenService.verifyExpiration(token);

        return jwtUtils.generateAccessTokenFromUsername(
                token.getUser().getUsername());
    }

    public void logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
    }

    public void logoutAll(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        refreshTokenService.deleteAllByUser(user);
    }
}