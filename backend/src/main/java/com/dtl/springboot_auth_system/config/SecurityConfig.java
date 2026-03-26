package com.dtl.springboot_auth_system.config;

import com.dtl.springboot_auth_system.security.jwt.AuthTokenFilter;
import com.dtl.springboot_auth_system.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity   // Cho phép dùng @PreAuthorize ở Controller
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    // Kết nối UserDetailsService + PasswordEncoder
    // Spring dùng cái này để load user và so sánh password khi login
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Dùng để trigger login trong AuthController
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // BCrypt để hash password khi register
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Tắt CSRF vì dùng JWT (stateless), không cần CSRF token
            .csrf(csrf -> csrf.disable())

            // Không dùng session, mỗi request phải tự mang JWT
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Cấu hình các endpoint được phép truy cập
            .authorizeHttpRequests(auth -> auth
                    // Cho phép không cần login
                    .requestMatchers("/api/auth/**").permitAll()
                    // Tất cả endpoint còn lại phải có JWT hợp lệ
                    .anyRequest().authenticated())

            // Đăng ký DaoAuthenticationProvider
            .authenticationProvider(authenticationProvider())

            // Chạy AuthTokenFilter TRƯỚC filter login mặc định của Spring
            .addFilterBefore(authTokenFilter, 
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}