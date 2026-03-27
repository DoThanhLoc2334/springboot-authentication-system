package com.dtl.springboot_auth_system.seeder;

import com.dtl.springboot_auth_system.model.Category;
import com.dtl.springboot_auth_system.model.Product;
import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.CategoryRepository;
import com.dtl.springboot_auth_system.repository.ProductRepository;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.util.RoleConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.username:admin}")
    private String adminUsername;

    @Value("${app.seed.admin.email:admin@gmail.com}")
    private String adminEmail;

    @Value("${app.seed.admin.password:123}")
    private String adminPassword;

    @PostConstruct
    public void init() {
        seedAdminUser();
        seedCatalog();
    }

    private void seedAdminUser() {
        if (userRepository.findByUsername(adminUsername).isPresent()) {
            return;
        }

        Role adminRole = roleRepository.findByName(RoleConstants.ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(RoleConstants.ADMIN)));

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }

    private void seedCatalog() {
        if (categoryRepository.count() > 0) {
            return;
        }

        Category laptop = categoryRepository.save(buildCategory("Laptop", "Office and gaming laptops."));
        Category smartphone = categoryRepository.save(buildCategory("Smartphone", "Modern smartphones."));

        productRepository.save(buildProduct(
                "MacBook Pro M3",
                45000000.0,
                "Powerful M3 chip with a Liquid Retina XDR display.",
                "https://images.unsplash.com/photo-1517336714460-4c50d11de3f7?q=80&w=600",
                laptop));
        productRepository.save(buildProduct(
                "iPhone 15 Pro Max",
                32000000.0,
                "Titanium frame and 5x optical zoom camera.",
                "https://images.unsplash.com/photo-1696446701796-da61225697cc?q=80&w=600",
                smartphone));
    }

    private Category buildCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return category;
    }

    private Product buildProduct(String name, Double price, String description, String imageUrl, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        return product;
    }
}
