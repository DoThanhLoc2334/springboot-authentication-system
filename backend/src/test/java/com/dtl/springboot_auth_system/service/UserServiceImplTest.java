package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.request.UserRequest;
import com.dtl.springboot_auth_system.exception.ForbiddenOperationException;
import com.dtl.springboot_auth_system.mapper.UserMapper;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.service.impl.UserServiceImpl;
import com.dtl.springboot_auth_system.util.RoleConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldPreventAdminFromDisablingOwnAccount() {
        User currentUser = buildAdminUser(1L, "admin");
        UserRequest request = new UserRequest();
        request.setUsername("admin");
        request.setEmail("admin@gmail.com");
        request.setPassword("new-password");
        request.setEnabled(false);
        request.setRoles(Set.of(RoleConstants.ADMIN));

        setAuthenticatedUser(currentUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));

        assertThrows(ForbiddenOperationException.class, () -> userService.updateUser(1L, request));
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldPreventAdminFromRemovingOwnAdminRole() {
        User currentUser = buildAdminUser(1L, "admin");
        UserRequest request = new UserRequest();
        request.setUsername("admin");
        request.setEmail("admin@gmail.com");
        request.setPassword("new-password");
        request.setEnabled(true);
        request.setRoles(Set.of(RoleConstants.USER));

        setAuthenticatedUser(currentUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));

        assertThrows(ForbiddenOperationException.class, () -> userService.updateUser(1L, request));
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldPreventAdminFromTogglingOwnStatus() {
        User currentUser = buildAdminUser(1L, "admin");

        setAuthenticatedUser(currentUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(currentUser));

        assertThrows(ForbiddenOperationException.class, () -> userService.toggleUserStatus(1L));
        SecurityContextHolder.clearContext();
    }

    private User buildAdminUser(Long id, String username) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(username + "@gmail.com");
        user.setPassword("encoded-password");
        user.setEnabled(true);
        user.setRoles(Set.of(new Role(RoleConstants.ADMIN)));
        try {
            var field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
        return user;
    }

    private void setAuthenticatedUser(User user) {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("ADMIN")
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    }
}
