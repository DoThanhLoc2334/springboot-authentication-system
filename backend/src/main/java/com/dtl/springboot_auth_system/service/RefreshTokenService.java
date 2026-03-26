package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.exception.TokenException;
import com.dtl.springboot_auth_system.model.RefreshToken;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    // Tạo refresh token mới cho user (mỗi lần login = 1 token mới)
    public RefreshToken createRefreshToken(User user, String deviceInfo) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpiration));
        refreshToken.setDeviceInfo(deviceInfo);

        return refreshTokenRepository.save(refreshToken);
    }

    // Validate refresh token — gọi trước khi cấp access token mới
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenException("Refresh token has expired. Please log in again.");
        }
        return token;
    }

    // Tìm token theo string — dùng khi client gửi refresh token lên
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenException("Refresh token invalid."));
    }

    // Lấy danh sách tất cả thiết bị đang đăng nhập của user
    public List<RefreshToken> getActiveTokensByUser(User user) {
        return refreshTokenRepository.findAllByUser(user);
    }

    // Logout thiết bị hiện tại
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    // Logout tất cả thiết bị
    @Transactional
    public void deleteAllByUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }
}