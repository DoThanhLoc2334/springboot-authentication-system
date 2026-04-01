package com.dtl.springboot_auth_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpResponse {

    private String message;
    private boolean success;

    public OtpResponse(String message) {
        this.message = message;
        this.success = true;
    }
}
