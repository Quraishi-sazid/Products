package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.apiModels.ICategoryProductResponse;
import com.example.hishab.hishabApp.model.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ICategoryRepository extends JpaRepository<Category,Integer> {
    void deleteCategoryByCategoryId(int categoryId);
    Category findCategoryByCategoryId(int categoryId);
    Category findCategoryByCategoryName(String categoryName);
    
    @Query(value = "SELECT cp.category_id as categoryId,cp.product_id as productId,cat.category_name as categoryName,pro.product_name as productName FROM tbl_cp_user_mapping u \r\n"
    		+ "INNER JOIN tbl_category_product_mapping cp ON cp.category_product_mapping_id = u.category_product_mapping_id\r\n"
    		+ "INNER join tbl_category cat ON cp.category_id = cat.category_id INNER JOIN tbl_product pro on pro.product_id = cp.product_id \r\n"
    		+ "WHERE user_id = ?1", nativeQuery = true)
    List<ICategoryProductResponse> findCategoryProduct(Integer userId);
}
