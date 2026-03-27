package com.dtl.springboot_auth_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "Ten san pham khong duoc de trong")
    private String name;

    @NotNull(message = "Gia tien khong duoc de trong")
    @PositiveOrZero(message = "Gia tien phai lon hon hoac bang 0")
    private Double price;

    private String description;
    private String imageUrl;

    @NotNull(message = "Danh muc khong duoc de trong")
    private Long categoryId;
}
