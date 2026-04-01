package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.UserDTO;
import com.dtl.springboot_auth_system.dto.request.ChangePasswordRequest;
import com.dtl.springboot_auth_system.dto.request.RefreshTokenRequest;
import com.dtl.springboot_auth_system.dto.response.JwtResponse;
import com.dtl.springboot_auth_system.security.CustomUserDetailsService;
import com.dtl.springboot_auth_system.security.JwtAuthenticationFilter;
import com.dtl.springboot_auth_system.security.RestAccessDeniedHandler;
import com.dtl.springboot_auth_system.security.RestAuthenticationEntryPoint;
import com.dtl.springboot_auth_system.security.SecurityConfig;
import com.dtl.springboot_auth_system.service.AuthService;
import com.dtl.springboot_auth_system.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = { AuthController.class, UserController.class, TestController.class })
@Import({ SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class })
class SecurityAuthorizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void shouldReturn401WhenProfileRequestedWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void shouldReturn403WhenUserRequestsAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldAllowAdminToAccessUserList() throws Exception {
        UserDTO adminUser = new UserDTO();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@gmail.com");
        adminUser.setEnabled(true);
        adminUser.setRoles(Set.of("ROLE_ADMIN"));
        when(userService.getAllUsers()).thenReturn(List.of(adminUser));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    @WithMockUser(username = "member", roles = "USER")
    void shouldAllowAuthenticatedUserToChangeOwnPassword() throws Exception {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("old-password");
        request.setNewPassword("new-password");
        doNothing().when(userService).changeCurrentUserPassword("old-password", "new-password");

        mockMvc.perform(put("/api/users/profile/password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).changeCurrentUserPassword("old-password", "new-password");
    }

    @Test
    void shouldAllowRefreshTokenWithoutAuthentication() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token");
        when(authService.refreshToken(any(RefreshTokenRequest.class)))
                .thenReturn(new JwtResponse("new-access-token", "new-refresh-token"));

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));

        verify(authService).refreshToken(any(RefreshTokenRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldAllowAdminToToggleUserStatus() throws Exception {
        doNothing().when(userService).toggleUserStatus(5L);

        mockMvc.perform(put("/api/users/5/status").with(csrf()))
                .andExpect(status().isOk());

        verify(userService).toggleUserStatus(eq(5L));
    }
}
