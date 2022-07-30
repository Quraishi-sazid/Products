package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ICategoryRepository extends JpaRepository<Category,Integer> {
    void deleteCategoryByCategoryId(int categoryId);
    Category findCategoryByCategoryId(int categoryId);
    Category findCategoryByCategoryName(String categoryName);
}
