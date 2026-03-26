package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.model.Role;
import com.dtl.springboot_auth_system.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role resolveRole(String roleName) {
        String roleKey = "ROLE_" +
                (roleName == null ? "USER" : roleName.toUpperCase());

        return roleRepository.findByName(roleKey)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại"));
    }
}