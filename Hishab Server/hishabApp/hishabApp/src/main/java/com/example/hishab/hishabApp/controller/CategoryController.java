package com.example.hishab.hishabApp.controller;

import com.example.hishab.hishabApp.model.Category;
import com.example.hishab.hishabApp.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequestMapping("/category/api")
@RestController
public class CategoryController {
    @Autowired
    ICategoryRepository categoryRepository;

    @PostMapping("/addOrUpdate")
    Category addOrUpdateCategory(@RequestParam int categoryId, @RequestParam String newCategoryName, @RequestParam String oldCategoryName) {
        if (categoryId == -1) {
            return addCategory(newCategoryName);
        } else {
           return categoryRepository.save(new Category(categoryId, newCategoryName));
        }
    }

    public Category addCategory(String newCategoryName) {
        Category existingCategory = categoryRepository.findCategoryByCategoryName(newCategoryName);
        if (existingCategory != null)
            return existingCategory;
        return ((Category) categoryRepository.save(new Category(newCategoryName)));
    }

    public Category findCategoryById(int categoryId) {
        return categoryRepository.findCategoryByCategoryId(categoryId);
    }
}
