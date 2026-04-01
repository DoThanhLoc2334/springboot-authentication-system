package com.dtl.springboot_auth_system.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String jwtSecretString,
            @Value("${app.jwt.access-token-expiration-ms:${app.jwt.expiration-ms:3600000}}") long accessTokenExpirationMs,
            @Value("${app.jwt.refresh-token-expiration-ms:604800000}") long refreshTokenExpirationMs) {

        this.jwtSecret = Keys.hmacShaKeyFor(jwtSecretString.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities,
            Integer tokenVersion) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + accessTokenExpirationMs);

        // Convert authorities to List of authority names
        Collection<String> authoritiesList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("type", "access")
                .claim("tokenVersion", tokenVersion == null ? 0 : tokenVersion)
                .claim("authorities", authoritiesList)
                .signWith(jwtSecret)
                .compact();
    }

    public String generateRefreshToken(String username, Integer tokenVersion) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + refreshTokenExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .claim("type", "refresh")
                .claim("tokenVersion", tokenVersion == null ? 0 : tokenVersion)
                .signWith(jwtSecret)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getTokenType(String token) {
        return getClaims(token).get("type", String.class);
    }

    public int getTokenVersion(String token) {
        Integer tokenVersion = getClaims(token).get("tokenVersion", Integer.class);
        return tokenVersion == null ? 0 : tokenVersion;
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token) && "access".equals(getTokenType(token));
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token) && "refresh".equals(getTokenType(token));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
