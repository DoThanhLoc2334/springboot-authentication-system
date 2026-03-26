package com.dtl.springboot_auth_system.repository;

import com.dtl.springboot_auth_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}