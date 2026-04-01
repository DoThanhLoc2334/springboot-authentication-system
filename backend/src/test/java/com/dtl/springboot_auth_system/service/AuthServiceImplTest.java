package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.request.RefreshTokenRequest;
import com.dtl.springboot_auth_system.dto.response.JwtResponse;
import com.dtl.springboot_auth_system.exception.InvalidCredentialsException;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.security.JwtTokenProvider;
import com.dtl.springboot_auth_system.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldRefreshTokenForEnabledUser() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid-refresh-token");
        UserDetails userDetails = new User(
                "admin",
                "encoded-password",
                true,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(tokenProvider.validateRefreshToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("admin");
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(userDetails);
        when(tokenProvider.generateAccessToken("admin", userDetails.getAuthorities())).thenReturn("new-access");
        when(tokenProvider.generateRefreshToken("admin")).thenReturn("new-refresh");

        JwtResponse response = authService.refreshToken(request);

        assertEquals("new-access", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());
    }

    @Test
    void shouldRejectRefreshTokenForDisabledUser() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid-refresh-token");
        UserDetails userDetails = new User(
                "locked-user",
                "encoded-password",
                false,
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        when(tokenProvider.validateRefreshToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("locked-user");
        when(userDetailsService.loadUserByUsername("locked-user")).thenReturn(userDetails);

        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));
        verify(userDetailsService).loadUserByUsername("locked-user");
    }

    @Test
    void shouldRejectInvalidRefreshToken() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("invalid-refresh-token");
        when(tokenProvider.validateRefreshToken("invalid-refresh-token")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));
    }
}
