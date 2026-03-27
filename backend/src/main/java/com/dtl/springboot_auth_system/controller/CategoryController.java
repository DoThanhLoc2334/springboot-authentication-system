package com.dtl.springboot_auth_system.controller;

import com.dtl.springboot_auth_system.dto.CategoryDTO;
import com.dtl.springboot_auth_system.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
