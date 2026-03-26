package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.request.LoginRequest;
import com.dtl.springboot_auth_system.dto.request.RegisterRequest;
import com.dtl.springboot_auth_system.dto.response.AuthResponse;
import com.dtl.springboot_auth_system.exception.TokenException;
import com.dtl.springboot_auth_system.model.RefreshToken;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Transactional
    public void register(RegisterRequest request) {
        // Kiểm tra username và email
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Gán role
        Role role = resolveRole(request.getRole());
        user.addRole(role);
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        // Xác thực username + password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo tokens
        String accessToken = jwtUtils.generateAccessToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        RefreshToken refreshToken = refreshTokenService
                .createRefreshToken(user, request.getDeviceInfo());

        // Lấy roles
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                userDetails.getUsername(),
                user.getEmail(),
                roles);
    }

    public String refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenService.findByToken(refreshToken);
        refreshTokenService.verifyExpiration(token);
        return jwtUtils.generateAccessTokenFromUsername(
                token.getUser().getUsername());
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
    }

    @Transactional
    public void logoutAll(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        refreshTokenService.deleteAllByUser(user);
    }

    // Resolve role từ string sang Role entity
    private Role resolveRole(String roleName) {
        if ("admin".equalsIgnoreCase(roleName)) {
            return roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role ADMIN không tồn tại"));
        } else if ("mod".equalsIgnoreCase(roleName)) {
            return roleRepository.findByName("ROLE_MODERATOR")
                    .orElseThrow(() -> new RuntimeException("Role MODERATOR không tồn tại"));
        } else {
            return roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Role USER không tồn tại"));
        }
    }
}