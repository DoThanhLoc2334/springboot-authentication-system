package com.dtl.springboot_auth_system.service.impl;

import com.dtl.springboot_auth_system.dto.ProductDTO;
import com.dtl.springboot_auth_system.dto.request.ProductRequest;
import com.dtl.springboot_auth_system.exception.ResourceNotFoundException;
import com.dtl.springboot_auth_system.mapper.ProductMapper;
import com.dtl.springboot_auth_system.model.Category;
import com.dtl.springboot_auth_system.model.Product;
import com.dtl.springboot_auth_system.repository.CategoryRepository;
import com.dtl.springboot_auth_system.repository.ProductRepository;
import com.dtl.springboot_auth_system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductRequest request) {
        Category category = findCategoryById(request.getCategoryId());
        Product product = productMapper.toEntity(request, category);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductRequest request) {
        Product product = findProductById(id);
        Category category = findCategoryById(request.getCategoryId());
        productMapper.updateEntity(product, request, category);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
