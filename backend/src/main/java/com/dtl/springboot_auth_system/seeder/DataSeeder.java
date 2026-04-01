package com.dtl.springboot_auth_system.seeder;

import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
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
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.username:admin}")
    private String adminUsername;

    @Value("${app.seed.admin.email:admin@gmail.com}")
    private String adminEmail;

    @Value("${app.seed.admin.password:123}")
    private String adminPassword;

    @PostConstruct
    public void init() {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        roleRepository.findByName(RoleConstants.USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleConstants.USER)));
        roleRepository.findByName(RoleConstants.ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(RoleConstants.ADMIN)));
    }

    private void seedAdminUser() {
        if (userRepository.findByUsername(adminUsername).isPresent()
                || userRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        Role adminRole = roleRepository.findByName(RoleConstants.ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(RoleConstants.ADMIN)));

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setEnabled(true);
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);
    }
}
