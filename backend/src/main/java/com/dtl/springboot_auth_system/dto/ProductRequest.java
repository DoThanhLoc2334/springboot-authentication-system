package com.dtl.springboot_auth_system.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Double price;
    private String description;
    private String imageUrl;
    private Long categoryId; // Gửi ID của danh mục lên để liên kết
}