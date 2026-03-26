package com.dtl.springboot_auth_system.dto.response;

import java.util.List;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private String email;
    private List<String> roles;

    public AuthResponse(String accessToken, String refreshToken,
                        String username, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
}