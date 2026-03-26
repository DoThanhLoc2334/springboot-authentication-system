package com.dtl.springboot_auth_system.seeder;

import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.model.Category;
import com.dtl.springboot_auth_system.model.Product;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.repository.CategoryRepository;
import com.dtl.springboot_auth_system.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSeeder {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Để mã hóa mật khẩu admin cho đúng chuẩn

    @PostConstruct
    public void init() {
        // 1. Seed Role và User Admin
        if (roleRepository.findByName("USER").isEmpty()) {
            Role roleUser = new Role("USER");
            roleRepository.save(roleUser);

            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@gmail.com");
            // Lưu ý: Nên mã hóa mật khẩu để có thể đăng nhập được qua Security
            user.setPassword(passwordEncoder.encode("123")); 
            user.setRoles(Set.of(roleUser));
            userRepository.save(user);

            System.out.println("Seeded User data!");
        }

        // 2. Seed Category và Product
        if (categoryRepository.count() == 0) {
            // Tạo Danh mục
            Category laptop = new Category();
            laptop.setName("Laptop");
            laptop.setDescription("Máy tính xách tay văn phòng và gaming");
            categoryRepository.save(laptop);

            Category smartphone = new Category();
            smartphone.setName("Smartphone");
            smartphone.setDescription("Điện thoại thông minh đời mới");
            categoryRepository.save(smartphone);

            // Tạo Sản phẩm 1
            Product p1 = new Product();
            p1.setName("MacBook Pro M3");
            p1.setPrice(45000000.0);
            p1.setDescription("Chip M3 mạnh mẽ, màn hình Liquid Retina XDR cực đẹp.");
            p1.setImageUrl("https://images.unsplash.com/photo-1517336714460-4c50d11de3f7?q=80&w=600");
            p1.setCategory(laptop);
            productRepository.save(p1);

            // Tạo Sản phẩm 2
            Product p2 = new Product();
            p2.setName("iPhone 15 Pro Max");
            p2.setPrice(32000000.0);
            p2.setDescription("Khung viền Titan, camera 5x zoom quang học.");
            p2.setImageUrl("https://images.unsplash.com/photo-1696446701796-da61225697cc?q=80&w=600");
            p2.setCategory(smartphone);
            productRepository.save(p2);

            System.out.println("Seeded Category & Product data!");
        }
    }
}