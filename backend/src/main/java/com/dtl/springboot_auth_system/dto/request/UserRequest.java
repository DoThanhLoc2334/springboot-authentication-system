package com.dtl.springboot_auth_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Set;

@Data
public class UserRequest {
    @NotBlank(message = "Username khong duoc de trong")
    @Size(min = 3, max = 50, message = "Username phai tu 3 den 50 ky tu")
    private String username;

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong hop le")
    private String email;

    @NotBlank(message = "Mat khau khong duoc de trong")
    @Size(min = 6, message = "Mat khau phai it nhat 6 ky tu")
    private String password;

    private boolean enabled = true;

    private Set<String> roles;
}