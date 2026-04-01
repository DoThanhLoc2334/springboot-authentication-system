package com.dtl.springboot_auth_system.service.impl;

import com.dtl.springboot_auth_system.dto.response.JwtResponse;
import com.dtl.springboot_auth_system.dto.request.LoginRequest;
import com.dtl.springboot_auth_system.dto.request.RefreshTokenRequest;
import com.dtl.springboot_auth_system.dto.request.RegisterRequest;
import com.dtl.springboot_auth_system.exception.InvalidCredentialsException;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.exception.ResourceNotFoundException;
import com.dtl.springboot_auth_system.exception.UserAlreadyExistsException;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.security.JwtTokenProvider;
import com.dtl.springboot_auth_system.service.AuthService;
import com.dtl.springboot_auth_system.util.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists.");
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
    public JwtResponse login(LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsernameOrEmail(),
                            request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Invalid username/email or password.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String username = authentication.getName();
        User user = findUserByUsername(username);

        String accessToken = tokenProvider.generateAccessToken(
                username,
                authentication.getAuthorities(),
                user.getTokenVersion());
        String refreshToken = tokenProvider.generateRefreshToken(username, user.getTokenVersion());

        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!tokenProvider.validateRefreshToken(refreshToken)) {
            throw new InvalidCredentialsException("Refresh token is invalid or expired.");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        User user = findUserByUsername(username);
        if (!user.isEnabled()) {
            throw new InvalidCredentialsException("Refresh token cannot be used for a disabled account.");
        }
        int tokenVersion = tokenProvider.getTokenVersion(refreshToken);
        if (user.getTokenVersion() == null || user.getTokenVersion() != tokenVersion) {
            throw new InvalidCredentialsException("Refresh token is invalid or has been revoked.");
        }

        UserDetails userDetails = buildUserDetails(user);
        String accessToken = tokenProvider.generateAccessToken(username, userDetails.getAuthorities(), user.getTokenVersion());
        String rotatedRefreshToken = tokenProvider.generateRefreshToken(username, user.getTokenVersion());

        return new JwtResponse(accessToken, rotatedRefreshToken);
    }

    @Override
    @Transactional
    public void logoutCurrentUser() {
        User currentUser = getAuthenticatedUser();
        int currentTokenVersion = currentUser.getTokenVersion() == null ? 0 : currentUser.getTokenVersion();
        currentUser.setTokenVersion(currentTokenVersion + 1);
        userRepository.save(currentUser);
        SecurityContextHolder.clearContext();
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid or expired token."));
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new InvalidCredentialsException("Authentication is required.");
        }
        return findUserByUsername(userDetails.getUsername());
    }

    private UserDetails buildUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(Role::getName).toArray(String[]::new))
                .disabled(!user.isEnabled())
                .build();
    }
}
