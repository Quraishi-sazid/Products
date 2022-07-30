package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.CategoryAndProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryAndProductMappingRepository extends JpaRepository<CategoryAndProductMapping,Integer> {

    CategoryAndProductMapping findFirstByCategoryCategoryIdAndProductProductId(int categoryId,int productId);
}
