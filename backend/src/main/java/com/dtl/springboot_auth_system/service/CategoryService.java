package com.dtl.springboot_auth_system.service;

import com.dtl.springboot_auth_system.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();
}
