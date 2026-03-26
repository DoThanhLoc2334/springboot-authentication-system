package com.dtl.springboot_auth_system.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    // Tạo SecretKey từ string secret
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Tạo Access Token từ Authentication object (sau khi login thành công)
    public String generateAccessToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return buildToken(userDetails.getUsername());
    }

    public String generateAccessTokenFromUsername(String username) {
        return buildToken(username);
    }

    private String buildToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("JWT expired: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims empty: {}", e.getMessage());
        }
        return false;
    }
}