package com.dtl.springboot_auth_system.security.jwt;

import com.dtl.springboot_auth_system.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
                
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Bước 1: Lấy JWT từ header
            String jwt = parseJwt(request);

            // Bước 2: Validate token
            if (jwt != null && jwtUtils.validateToken(jwt)) {

                // Bước 3: Lấy username từ token
                String username = jwtUtils.getUsernameFromToken(jwt);

                // Bước 4: Load user từ DB
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Bước 5: Tạo Authentication object
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // Bước 6: Lưu vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Không thể xác thực user: {}", e.getMessage());
        }

        // Bước 7: Chuyển tiếp request dù token có hợp lệ hay không
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Bỏ "Bearer " lấy token
        }

        return null;
    }
}