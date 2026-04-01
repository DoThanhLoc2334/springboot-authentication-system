package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.request.RefreshTokenRequest;
import com.dtl.springboot_auth_system.dto.response.JwtResponse;
import com.dtl.springboot_auth_system.exception.InvalidCredentialsException;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.security.JwtTokenProvider;
import com.dtl.springboot_auth_system.service.impl.AuthServiceImpl;
import com.dtl.springboot_auth_system.util.RoleConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldRefreshTokenForEnabledUser() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid-refresh-token");
        User user = buildUser("admin", true, 3, Set.of(new Role(RoleConstants.ADMIN)));

        when(tokenProvider.validateRefreshToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("admin");
        when(tokenProvider.getTokenVersion("valid-refresh-token")).thenReturn(3);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(tokenProvider.generateAccessToken("admin", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")), 3))
                .thenReturn("new-access");
        when(tokenProvider.generateRefreshToken("admin", 3)).thenReturn("new-refresh");

        JwtResponse response = authService.refreshToken(request);

        assertEquals("new-access", response.getAccessToken());
        assertEquals("new-refresh", response.getRefreshToken());
    }

    @Test
    void shouldRejectRefreshTokenForDisabledUser() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid-refresh-token");
        User user = buildUser("locked-user", false, 1, Set.of(new Role(RoleConstants.USER)));

        when(tokenProvider.validateRefreshToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("locked-user");
        when(userRepository.findByUsername("locked-user")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));
        verify(userRepository).findByUsername("locked-user");
    }

    @Test
    void shouldRejectInvalidRefreshToken() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("invalid-refresh-token");
        when(tokenProvider.validateRefreshToken("invalid-refresh-token")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));
    }

    @Test
    void shouldRejectRefreshTokenWhenUserDoesNotExist() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid-refresh-token");

        when(tokenProvider.validateRefreshToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("ghost");
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));
    }

    @Test
    void shouldRejectRevokedRefreshTokenVersion() {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid-refresh-token");
        User user = buildUser("admin", true, 5, Set.of(new Role(RoleConstants.ADMIN)));

        when(tokenProvider.validateRefreshToken("valid-refresh-token")).thenReturn(true);
        when(tokenProvider.getUsernameFromToken("valid-refresh-token")).thenReturn("admin");
        when(tokenProvider.getTokenVersion("valid-refresh-token")).thenReturn(4);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class, () -> authService.refreshToken(request));
    }

    private User buildUser(String username, boolean enabled, int tokenVersion, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@gmail.com");
        user.setPassword("encoded-password");
        user.setEnabled(enabled);
        user.setTokenVersion(tokenVersion);
        user.setRoles(roles);
        return user;
    }
}
