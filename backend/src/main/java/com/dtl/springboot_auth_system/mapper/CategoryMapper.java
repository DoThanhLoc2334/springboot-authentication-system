package com.dtl.springboot_auth_system.mapper;

import com.dtl.springboot_auth_system.dto.CategoryDTO;
import com.dtl.springboot_auth_system.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }
}
