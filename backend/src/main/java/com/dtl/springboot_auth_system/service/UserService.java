package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.UserDTO;
import com.dtl.springboot_auth_system.dto.request.UserRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    Page<UserDTO> getAllUsers(Pageable pageable);

    UserDTO getUserById(Long id);

    UserDTO createUser(UserRequest request);

    UserDTO updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    void changePassword(Long id, String newPassword);

    void changeCurrentUserPassword(String currentPassword, String newPassword);

    void toggleUserStatus(Long id);

    UserDTO getCurrentUser();
}
