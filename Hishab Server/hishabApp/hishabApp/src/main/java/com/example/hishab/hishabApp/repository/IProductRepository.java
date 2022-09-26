package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.CPUserMapping;
import com.example.hishab.hishabApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product,Integer> {
    Product findFirstByProductName(String productName);
    
    @Query(value ="Select * from tbl_product where product_name =:productName limit 1 ",nativeQuery = true)
    Product findFirstByProductNameNative(@Param("productName") String productName);
    
    @Query(value ="Select product_id from tbl_product where product_name =:productName limit 1 ",nativeQuery = true)
    Integer findFirstByProductNameNativetest(@Param("productName") String productName);
}
