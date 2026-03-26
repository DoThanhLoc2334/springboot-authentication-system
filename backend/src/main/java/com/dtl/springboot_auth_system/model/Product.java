package com.dtl.springboot_auth_system.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    private Double price;

    private String imageUrl;

    // Nhiều sản phẩm thuộc về một danh mục
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}