package com.dtl.springboot_auth_system.service.impl;

import com.dtl.springboot_auth_system.dto.RegisterRequest;
import com.dtl.springboot_auth_system.dto.LoginRequest;
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
        // 1. Kiểm tra trùng lặp
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username đã tồn tại!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email đã tồn tại!");
        }

        // 2. Lấy Role mặc định
        Role roleUser = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Lỗi: Role USER không tìm thấy trong Database"));

        // 3. Tạo User mới
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa mật khẩu
        user.setRoles(Set.of(roleUser));

        // 4. Lưu vào Database
        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest request) {
        // 1. Tìm User theo Username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Tài khoản hoặc mật khẩu không chính xác"));

        // 2. Kiểm tra mật khẩu khớp với bản mã hóa trong DB không
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Tài khoản hoặc mật khẩu không chính xác");
        }

        // 3. Nếu mọi thứ OK, tạo Token "quyền lực" và trả về
        return tokenProvider.generateToken(user.getUsername());
    }
}