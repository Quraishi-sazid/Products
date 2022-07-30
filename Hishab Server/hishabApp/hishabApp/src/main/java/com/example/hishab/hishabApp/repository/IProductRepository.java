package com.example.hishab.hishabApp.repository;

import com.example.hishab.hishabApp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product,Integer> {
    Product findFirstByProductName(String productName);
}
