package com.example.hishab.hishabApp.controller;

import com.example.hishab.hishabApp.model.*;
import com.example.hishab.hishabApp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product/api")
public class ProductController {
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private CategoryController categoryController;

    @Autowired
    private ICategoryAndProductMappingRepository categoryAndProductMappingRepository;

    @Autowired
    private CPUserRepository cpUserRepository;

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/addOrUpdateProduct")
    public Product addOrUpdateProduct(@RequestParam int productId, @RequestParam int categoryId, @RequestParam String categoryName, @RequestParam String productName, @RequestParam Integer userId) {
        Product existingProduct = productRepository.findFirstByProductName(productName);
        UserModel userModel = userRepository.findUserModelByUserId(userId);
        if (existingProduct == null) {
            return insertNewProduct(categoryId, categoryName, productName, userModel);
        } else {
            Category productCategory;
            if (categoryId == -1)
                productCategory = categoryController.addCategory(categoryName);
            else
                productCategory = categoryController.findCategoryById(categoryId);
            if (categoryAndProductMappingRepository.findFirstByCategoryCategoryIdAndProductProductId(productCategory.getCategoryId(), existingProduct.getProductId()) == null) {
                updateMappingTable(userModel, productCategory, existingProduct);
                return existingProduct;
            } else {
                return insertProductWithMappingTables(userModel, productCategory, productName, existingProduct);
            }
        }
    }

    private Product insertNewProduct(int categoryId, String categoryName, String productName, UserModel userModel) {
        Category productCategory;
        if (categoryId == -1)
            productCategory = categoryController.addCategory(categoryName);
        else
            productCategory = new Category(categoryId, categoryName);
        Product insertedProduct = insertProductWithMappingTables(userModel, productCategory, productName, null);
        return insertedProduct;
    }

    private Product insertProductWithMappingTables(UserModel userModel, Category productCategory, String productName, Product existingProduct) {
        if (existingProduct == null)
            existingProduct = productRepository.save(new Product(productName));
        updateMappingTable(userModel, productCategory, existingProduct);
        return existingProduct;
    }

    private void updateMappingTable(UserModel userModel, Category productCategory, Product existingProduct) {
        CategoryAndProductMapping categoryAndProductMapping = categoryAndProductMappingRepository.findFirstByCategoryCategoryIdAndProductProductId(productCategory.getCategoryId(), existingProduct.getProductId());
        if (categoryAndProductMapping == null)
            categoryAndProductMapping = categoryAndProductMappingRepository.save(new CategoryAndProductMapping(productCategory, existingProduct));
        var cpUserMapping = cpUserRepository.findUserMappingByProductId(existingProduct.getProductId(), userModel.getUserId());
        if (cpUserMapping != null) {
            cpUserMapping.setCategoryProductMapping(categoryAndProductMapping);
            cpUserRepository.save(cpUserMapping);
        } else
            cpUserRepository.save(new CPUserMapping(categoryAndProductMapping, userModel));
    }

/*    @GetMapping("/productByCategoryId/{categoryId}")
    public List<Product> getProductByCategoryId(@PathVariable int categoryId) {
        return productRepository.findProductsByCategoryCategoryId(categoryId);
    }*/
}
