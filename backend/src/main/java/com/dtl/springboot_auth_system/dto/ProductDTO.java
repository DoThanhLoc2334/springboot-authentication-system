package com.dtl.springboot_auth_system.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String imageUrl;
    private String categoryName; // Chỉ gửi tên danh mục về cho nhẹ
}