package com.dtl.springboot_auth_system.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String message,
        LocalDateTime timestamp,
        Map<String, String> errors // Dùng để liệt kê lỗi Validation chi tiết
) {
}