package com.example.hishab.hishabApp.controller;

import com.example.hishab.hishabApp.apiModels.CategoryResponse;
import com.example.hishab.hishabApp.apiModels.ProductRequest;
import com.example.hishab.hishabApp.apiModels.ProductResponse;
import com.example.hishab.hishabApp.model.*;
import com.example.hishab.hishabApp.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ProductResponse addOrUpdateProduct(@RequestBody ProductRequest productRequest) {
        Product existingProduct = productRepository.findFirstByProductName(productRequest.productName);
        UserModel userModel = userRepository.findUserModelByUserId(productRequest.userId);
        if (existingProduct == null) {
            return getProductResponse(productRequest, insertNewProduct(productRequest.categoryRequest.getCategoryId(), productRequest.categoryRequest.getNewCategoryName(), productRequest.productName, userModel)); 
        } else {
            Category productCategory;
            if (productRequest.getCategoryRequest().getCategoryId() == -1)
                productCategory = categoryController.addCategory(productRequest.categoryRequest.getNewCategoryName());
            else
                productCategory = categoryController.findCategoryById(productRequest.categoryRequest.getCategoryId());
            if (categoryAndProductMappingRepository.findFirstByCategoryCategoryIdAndProductProductId(productCategory.getCategoryId(), existingProduct.getProductId()) == null) {
                updateMappingTable(userModel, productCategory, existingProduct);
                existingProduct.categoryId = productCategory.getCategoryId();
                return getProductResponse(productRequest, existingProduct);
            } else {
                Product product= insertProductWithMappingTables(userModel, productCategory, productRequest.productName, existingProduct);
                product.categoryId = productCategory.getCategoryId();
                return getProductResponse(productRequest, product);
            }
        }
    }

	private ProductResponse getProductResponse(ProductRequest productRequest, Product product) {
		return new ProductResponse(product,productRequest.localId, new CategoryResponse(productRequest.categoryRequest.getLocalId(),product.categoryId));
	}

    private Product insertNewProduct(int categoryId, String categoryName, String productName, UserModel userModel) {
        Category productCategory;
        if (categoryId == -1)
            productCategory = categoryController.addCategory(categoryName);
        else
            productCategory = new Category(categoryId, categoryName);
        Product insertedProduct = insertProductWithMappingTables(userModel, productCategory, productName, null);
        insertedProduct.categoryId = productCategory.getCategoryId();
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
        CPUserMapping cpUserMapping = cpUserRepository.findUserMappingByProductId(existingProduct.getProductId(), userModel.getUserId());
        if (cpUserMapping != null) {
            cpUserMapping.setCategoryProductMapping(categoryAndProductMapping);
            cpUserRepository.save(cpUserMapping);
        } else
            cpUserRepository.save(new CPUserMapping(categoryAndProductMapping, userModel));
    }
    
    @PostMapping("/addOrUpdateProductList")
    public List<ProductResponse> addOrUpdateProductList(@RequestBody List<ProductRequest> productRequestList) {
    	List<ProductResponse> responseList = new ArrayList<ProductResponse>();
    	productRequestList.forEach(productRequest ->{
    		ProductResponse productRespose = addOrUpdateProduct(productRequest);
    		productRespose.getProduct().productName = "";
    		responseList.add(productRespose);
    	});
    	return responseList;
    }
}
