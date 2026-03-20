package com.dtl.springboot_auth_system.repository;

import com.dtl.springboot_auth_system.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}