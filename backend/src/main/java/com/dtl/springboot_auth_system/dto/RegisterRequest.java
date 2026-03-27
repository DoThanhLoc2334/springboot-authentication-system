package com.dtl.springboot_auth_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username khong duoc de trong")
    private String username;

    @NotBlank(message = "Email khong duoc de trong")
    @Email(message = "Email khong dung dinh dang")
    private String email;

    @NotBlank(message = "Mat khau khong duoc de trong")
    @Size(min = 6, message = "Mat khau phai co it nhat 6 ky tu")
    private String password;
}
