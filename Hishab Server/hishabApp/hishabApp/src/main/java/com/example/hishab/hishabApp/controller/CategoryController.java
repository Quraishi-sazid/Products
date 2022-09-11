package com.example.hishab.hishabApp.controller;

import com.example.hishab.hishabApp.apiModels.CategoryRequest;
import com.example.hishab.hishabApp.apiModels.CategoryResponse;
import com.example.hishab.hishabApp.model.Category;
import com.example.hishab.hishabApp.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/category/api")
@RestController
public class CategoryController {
    @Autowired
    ICategoryRepository categoryRepository;

    @PostMapping("/addOrUpdate")
    CategoryResponse addOrUpdateRequest(@RequestBody CategoryRequest categoryRequest) {
    	Category category = addOrUpdateCategory(categoryRequest);
    	return new CategoryResponse(categoryRequest.getLocalId(), category.getCategoryId());
    }
    
    
    Category addOrUpdateCategory(@RequestBody CategoryRequest categoryRequest) {
        if (categoryRequest.getCategoryId() == -1) {
            return addCategory(categoryRequest.getNewCategoryName());
        } else {
           return categoryRepository.save(new Category(categoryRequest.getCategoryId(), categoryRequest.getNewCategoryName()));
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
    @PostMapping("/addOrUpdateList")
    List<CategoryResponse> addOrUpdateCategoryList(@RequestBody List<CategoryRequest> categoryRequestList) {
    	List<CategoryResponse>responseList = new ArrayList<CategoryResponse>();
    	categoryRequestList.forEach(categoryRequest ->{
    		Category category= addOrUpdateCategory(categoryRequest);
    		responseList.add(new CategoryResponse(categoryRequest.getLocalId(), category.getCategoryId()));
    	});
    	return responseList;
    }
    
}
