package com.dtl.springboot_auth_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "OTP must not be blank")
    private String otp;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;

    @NotBlank(message = "Confirm password must not be blank")
    private String confirmPassword;
}
