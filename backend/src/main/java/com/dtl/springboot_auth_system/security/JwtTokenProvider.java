package com.dtl.springboot_auth_system.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Tạo một Key bí mật (Trong thực tế nên lưu trong application.properties)
    private final String jwtSecretString = "phai_co_it_nhat_32_ky_tu_cho_security_2026";
    private final SecretKey jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
    private final long jwtExpirationDate = 86400000; // 24 giờ

    // 1. Tạo Token từ Username
    public String generateToken(String username) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        return Jwts.builder()
                .subject(username) // Thay cho setSubject()
                .issuedAt(currentDate) // Thay cho setIssuedAt()
                .expiration(expireDate) // Thay cho setExpiration()
                .signWith(jwtSecret) // JJWT tự nhận diện thuật toán
                .compact();
    }

    // 2. Lấy Username từ Token
    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecret) // Thay cho setSigningKey()
                .build()
                .parseSignedClaims(token) // Thay cho parseClaimsJws()
                .getPayload() // Thay cho getBody()
                .getSubject();
    }

    // 3. Kiểm tra Token hợp lệ không
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            // Log lỗi cụ thể ở đây nếu cần (Expired, Malformed...)
            return false;
        }
    }
}