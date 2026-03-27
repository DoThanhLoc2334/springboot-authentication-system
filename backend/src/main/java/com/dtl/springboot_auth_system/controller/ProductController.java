package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.ProductDTO;
import com.dtl.springboot_auth_system.dto.ProductRequest;
import com.dtl.springboot_auth_system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
