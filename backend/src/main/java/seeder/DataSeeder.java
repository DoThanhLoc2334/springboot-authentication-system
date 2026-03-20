package com.dtl.springboot_auth_system.seeder;

import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSeeder {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {

        // tránh insert trùng
        if (roleRepository.findByName("USER").isEmpty()) {
            Role roleUser = new Role("USER");
            roleRepository.save(roleUser);

            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@gmail.com");
            user.setPassword("123");

            user.setRoles(Set.of(roleUser));

            userRepository.save(user);

            System.out.println("Seeded test data!");
        }
    }
} 
