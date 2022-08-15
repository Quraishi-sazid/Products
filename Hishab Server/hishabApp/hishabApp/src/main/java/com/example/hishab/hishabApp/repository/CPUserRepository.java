package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.CPUserMapping;
import com.example.hishab.hishabApp.model.CategoryAndProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CPUserRepository extends JpaRepository<CPUserMapping,Integer> {
    CPUserMapping findCPUserMappingByCategoryProductMapping_CategoryProductMappingId(int mappingId);
    @Query(value ="select u.* FROM tbl_category_product_mapping cp inner join tbl_cp_user_mapping  u ON\n" +
            "cp.category_product_mapping_id = u.category_product_mapping_id WHERE cp.product_id=:productId and u.user_id=:userId",nativeQuery = true)
    CPUserMapping findUserMappingByProductId(@Param("productId") Integer productId,@Param("userId") Integer userId);
}
