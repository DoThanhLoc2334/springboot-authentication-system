package com.dtl.springboot_auth_system.mapper;

import com.dtl.springboot_auth_system.dto.ProductDTO;
import com.dtl.springboot_auth_system.dto.request.ProductRequest;
import com.dtl.springboot_auth_system.model.Category;
import com.dtl.springboot_auth_system.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDto(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        return dto;
    }

    public Product toEntity(ProductRequest request, Category category) {
        Product product = new Product();
        updateEntity(product, request, category);
        return product;
    }

    public void updateEntity(Product product, ProductRequest request, Category category) {
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
    }
}
