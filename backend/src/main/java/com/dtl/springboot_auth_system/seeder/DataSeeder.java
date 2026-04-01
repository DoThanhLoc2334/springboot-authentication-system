package com.dtl.springboot_auth_system.seeder;

import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.model.User;
import com.dtl.springboot_auth_system.repository.RoleRepository;
import com.dtl.springboot_auth_system.repository.UserRepository;
import com.dtl.springboot_auth_system.util.RoleConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin.enabled:false}")
    private boolean adminSeedEnabled;

    @Value("${app.seed.admin.username:}")
    private String adminUsername;

    @Value("${app.seed.admin.email:}")
    private String adminEmail;

    @Value("${app.seed.admin.password:}")
    private String adminPassword;

    @PostConstruct
    public void init() {
        seedRoles();
        normalizeUserTokenVersions();
        seedAdminUser();
    }

    private void seedRoles() {
        roleRepository.findByName(RoleConstants.USER)
                .orElseGet(() -> roleRepository.save(new Role(RoleConstants.USER)));
        roleRepository.findByName(RoleConstants.ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(RoleConstants.ADMIN)));
    }

    private void seedAdminUser() {
        if (!adminSeedEnabled) {
            log.info("Admin seeding is disabled.");
            return;
        }

        if (!StringUtils.hasText(adminUsername)
                || !StringUtils.hasText(adminEmail)
                || !StringUtils.hasText(adminPassword)) {
            log.warn("Admin seeding is enabled, but username/email/password is missing. Skipping admin seed.");
            return;
        }

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

    private void normalizeUserTokenVersions() {
        userRepository.findAll().stream()
                .filter(user -> user.getTokenVersion() == null)
                .forEach(user -> {
                    user.setTokenVersion(0);
                    userRepository.save(user);
                });
    }
}
