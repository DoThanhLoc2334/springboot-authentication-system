package com.dtl.springboot_auth_system.service.impl;

import com.dtl.springboot_auth_system.dto.CategoryDTO;
import com.dtl.springboot_auth_system.mapper.CategoryMapper;
import com.dtl.springboot_auth_system.repository.CategoryRepository;
import com.dtl.springboot_auth_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .toList();
    }
}
