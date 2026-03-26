package com.dtl.springboot_auth_system.exception;

// Phải kế thừa RuntimeException để Spring có thể bắt được lỗi này
public class UserAlreadyExistsException extends RuntimeException {
    
    // Thêm constructor nhận message để truyền nội dung lỗi
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}