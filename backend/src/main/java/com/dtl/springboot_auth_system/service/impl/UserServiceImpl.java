package com.dtl.springboot_auth_system.service.impl;

import com.dtl.springboot_auth_system.dto.UserDTO;
import com.dtl.springboot_auth_system.dto.request.UserRequest;
import com.dtl.springboot_auth_system.exception.ForbiddenOperationException;
import com.dtl.springboot_auth_system.exception.ResourceNotFoundException;
import com.dtl.springboot_auth_system.exception.UserAlreadyExistsException;
import com.dtl.springboot_auth_system.mapper.UserMapper;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.service.UserService;
import com.dtl.springboot_auth_system.util.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDTO createUser(UserRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        User user = userMapper.toEntity(request);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserRequest request) {
        User user = findUserById(id);
        User currentUser = getAuthenticatedUser();

        // Check if username or email conflicts with other users
        if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        validateSelfAdminUpdate(user, currentUser, request);
        userMapper.updateEntity(user, request);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    @Override
    public UserDTO getCurrentUser() {
        return userMapper.toDto(getAuthenticatedUser());
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = findUserById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changeCurrentUserPassword(String currentPassword, String newPassword) {
        User currentUser = getAuthenticatedUser();
        if (!passwordEncoder.matches(currentPassword, currentUser.getPassword())) {
            throw new ForbiddenOperationException("Current password is incorrect.");
        }
        if (passwordEncoder.matches(newPassword, currentUser.getPassword())) {
            throw new ForbiddenOperationException("New password must be different from the current password.");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }

    @Override
    @Transactional
    public void toggleUserStatus(Long id) {
        User user = findUserById(id);
        User currentUser = getAuthenticatedUser();
        if (user.getId().equals(currentUser.getId())) {
            throw new ForbiddenOperationException("You cannot disable your own account.");
        }
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        }
        throw new ResourceNotFoundException("No authenticated user found");
    }

    private void validateSelfAdminUpdate(User targetUser, User currentUser, UserRequest request) {
        if (!targetUser.getId().equals(currentUser.getId())) {
            return;
        }
        if (!request.isEnabled()) {
            throw new ForbiddenOperationException("You cannot disable your own account.");
        }
        if (request.getRoles() != null && !request.getRoles().contains(RoleConstants.ADMIN)) {
            throw new ForbiddenOperationException("You cannot remove your own admin role.");
        }
    }
}
