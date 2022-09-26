package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.Product
import com.example.hishab.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCategoryMappingViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    lateinit var catagoryList: List<Category>
    fun getProductCategoryListInnerJoin(): LiveData<List<CategoryAndProductModel>> {
        return productRepository.getProductCategoryListInnerJoin()
    }

    fun getProductListFromCategoryIdInnerJoin(categoryID : Long): LiveData<List<CategoryAndProductModel>> {
        return productRepository.getProductListFromCategoryIdInnerJoin(categoryID)
    }

    suspend fun insertCategoryProductMapping(
        categoryId: Long,
        categoryName: String,
        productName: String
    ) {
        var product = productRepository.getProductIdByInsertingInDataBase(categoryId, categoryName, productName)
        productRepository.saveToRemote(product)
    }

    fun deleteProduct(productId: Long) {
        productRepository.deleteByProductById(productId)
    }

    fun updateProductName(productId: Long, productName: String,categoryID: Long) {
        productRepository.updateProductName(productId, productName)
        viewModelScope.launch {
            productRepository.saveToRemote(Product(productName,categoryID,productId))
        }
    }

    fun getPurchaseCountOfProductId(productId: Long): Int {
        return productRepository.getPurchaseCountOfProductId(productId)
    }

    suspend fun getAllCategories() {
        catagoryList = productRepository.categoryRepository.getAllCategoriesSuspended()
    }

    fun getCategorySeparatedProductList(productCategoryMappingList: List<CategoryAndProductModel>): List<Any> {
        var returnList = ArrayList<Any>()
        if (productCategoryMappingList.size == 0)
            return returnList
        var lastCategoryId = productCategoryMappingList[0].getCategoryId()
        returnList.add(productCategoryMappingList[0].getCategoryName()!!)
        productCategoryMappingList.forEach { categoryAndProductModel ->
            if (categoryAndProductModel.getCategoryId() != lastCategoryId)
                returnList.add(categoryAndProductModel.getCategoryName()!!)
            returnList.add(categoryAndProductModel)
            lastCategoryId = categoryAndProductModel.getCategoryId()
        }
        return returnList
    }

    @Inject
    lateinit var productRepository: ProductRepository
}