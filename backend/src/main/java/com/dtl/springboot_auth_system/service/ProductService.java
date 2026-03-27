package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.ProductDTO;
import com.dtl.springboot_auth_system.dto.ProductRequest;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO createProduct(ProductRequest request);

    ProductDTO updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);
}
