package com.dtl.springboot_auth_system.service.impl;

import com.dtl.springboot_auth_system.dto.LoginRequest;
import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.exception.InvalidCredentialsException;
import com.dtl.springboot_auth_system.exception.ResourceNotFoundException;
import com.dtl.springboot_auth_system.exception.UserAlreadyExistsException;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.security.JwtTokenProvider;
import com.dtl.springboot_auth_system.security.RoleConstants;
import com.dtl.springboot_auth_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username da ton tai.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email da ton tai.");
        }

        Role roleUser = roleRepository.findByName(RoleConstants.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default user role was not found."));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(roleUser));

        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Tai khoan hoac mat khau khong chinh xac."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Tai khoan hoac mat khau khong chinh xac.");
        }

        return tokenProvider.generateToken(user.getUsername());
    }
}
