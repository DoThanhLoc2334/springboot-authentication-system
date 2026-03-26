package com.dtl.springboot_auth_system.service.impl;

import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.dto.LoginRequest;
import com.dtl.springboot_auth_system.exception.UserAlreadyExistsException;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor // Tự tạo Constructor cho các field final (Thay cho @Autowired)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {
        // Thay vì return String, ta ném Exception để GlobalExceptionHandler "tóm" lấy
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username đã tồn tại!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email đã tồn tại!");
        }

        Role roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER không tìm thấy"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(roleUser));

        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {
        // 1. Kiểm tra Username có tồn tại không
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Tài khoản hoặc mật khẩu không chính xác"));

        // 2. Kiểm tra mật khẩu (Sử dụng matches của BCrypt)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Tài khoản hoặc mật khẩu không chính xác");
        }

        // 3. Kiểm tra trạng thái tài khoản (nếu có trường enabled)
        // if (!user.isEnabled()) { throw new RuntimeException("Tài khoản đã bị khóa");
        // }

        return "Đăng nhập thành công! (Chờ tích hợp JWT)";
    }
}