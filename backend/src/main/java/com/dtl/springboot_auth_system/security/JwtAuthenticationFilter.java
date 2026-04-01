package com.dtl.springboot_auth_system.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.model.User;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j   // Dùng lombok log thay vì logger thủ công
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = resolveToken(request);

            if (StringUtils.hasText(jwt)
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && tokenProvider.validateAccessToken(jwt)) {

                String username = tokenProvider.getUsernameFromToken(jwt);
                int tokenVersion = tokenProvider.getTokenVersion(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                User user = userRepository.findByUsername(username).orElse(null);

                if (!userDetails.isEnabled()) {
                    throw new DisabledException("User account is disabled.");
                }
                if (user == null || user.getTokenVersion() == null || user.getTokenVersion() != tokenVersion) {
                    throw new IllegalStateException("Token has been revoked.");
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
