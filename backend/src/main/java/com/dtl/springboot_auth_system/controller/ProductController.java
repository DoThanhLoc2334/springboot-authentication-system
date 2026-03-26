package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.ProductDTO;
import com.dtl.springboot_auth_system.model.Product;
import com.dtl.springboot_auth_system.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dtl.springboot_auth_system.dto.ProductRequest;
import com.dtl.springboot_auth_system.model.Category;
import com.dtl.springboot_auth_system.repository.CategoryRepository;

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

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductRequest request) {
        // 1. Tìm Category dựa trên ID gửi lên
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục!"));

        // 2. Chuyển từ Request sang Entity
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        // 3. Lưu vào Database
        Product savedProduct = productRepository.save(product);

        // 4. Trả về DTO cho Frontend (để hiển thị ngay lập tức)
        ProductDTO dto = new ProductDTO();
        dto.setId(savedProduct.getId());
        dto.setName(savedProduct.getName());
        dto.setPrice(savedProduct.getPrice());
        dto.setCategoryName(category.getName());
        dto.setImageUrl(savedProduct.getImageUrl());

        return dto;
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        // Spring Data JPA sẽ tự động tìm và xóa, nếu không có ID này nó sẽ không làm gì
        // cả
        productRepository.deleteById(id);
    }


    @PutMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        // 1. Kiểm tra xem sản phẩm có tồn tại không
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        // 2. Tìm danh mục mới (đề phòng trường hợp người dùng đổi danh mục cho sản
        // phẩm)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục!"));

        // 3. Cập nhật thông tin từ request vào entity
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        // 4. Lưu sản phẩm đã cập nhật
        Product updatedProduct = productRepository.save(product);

        // 5. Trả về DTO để Frontend hiển thị lại
        ProductDTO dto = new ProductDTO();
        dto.setId(updatedProduct.getId());
        dto.setName(updatedProduct.getName());
        dto.setPrice(updatedProduct.getPrice());
        dto.setDescription(updatedProduct.getDescription());
        dto.setImageUrl(updatedProduct.getImageUrl());
        dto.setCategoryName(category.getName());

        return dto;
    }
}