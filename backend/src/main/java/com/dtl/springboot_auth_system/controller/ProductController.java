package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.ProductDTO;
import com.dtl.springboot_auth_system.model.Product;
import com.dtl.springboot_auth_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(product -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setDescription(product.getDescription());
            dto.setImageUrl(product.getImageUrl());
            dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : "No Category");
            return dto;
        }).collect(Collectors.toList());
    }
}