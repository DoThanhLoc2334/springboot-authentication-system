package com.dtl.springboot_auth_system.mapper;

import com.dtl.springboot_auth_system.dto.UserDTO;
import com.dtl.springboot_auth_system.dto.request.CreateUserRequest;
import com.dtl.springboot_auth_system.dto.request.UpdateUserRequest;
import com.dtl.springboot_auth_system.exception.ResourceNotFoundException;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.util.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.isEnabled());
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(request.isEnabled());
        user.setRoles(resolveRolesOrDefault(request.getRoles()));
        return user;
    }

    public void updateEntity(User user, UpdateUserRequest request) {
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        user.setEnabled(request.isEnabled());
        if (request.getRoles() != null) {
            user.setRoles(resolveRolesOrDefault(request.getRoles()));
        }
    }

    private Set<Role> resolveRolesOrDefault(Set<String> roleNames) {
        if (roleNames != null && !roleNames.isEmpty()) {
            return roleNames.stream()
                    .map(this::findRoleByName)
                    .collect(Collectors.toSet());
        }

        Set<Role> defaultRoles = new HashSet<>();
        defaultRoles.add(findDefaultUserRole());
        return defaultRoles;
    }

    private Role findDefaultUserRole() {
        return roleRepository.findByName(RoleConstants.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default user role was not found."));
    }

    private Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
    }
}
