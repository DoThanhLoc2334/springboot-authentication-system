package com.dtl.springboot_auth_system.service.impl;

import com.dtl.springboot_auth_system.dto.LoginRequest;
import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.exception.InvalidCredentialsException;
import com.dtl.springboot_auth_system.exception.UserAlreadyExistsException;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.security.JwtTokenProvider;
import com.dtl.springboot_auth_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email đã tồn tại!");
        }

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role ROLE_USER không tìm thấy trong DB"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(roleUser));

        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Tài khoản hoặc mật khẩu không chính xác"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Tài khoản hoặc mật khẩu không chính xác");
        }

        return tokenProvider.generateToken(user.getUsername());
    }
}