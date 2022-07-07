package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.entities.Category
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductCategoryMappingViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    lateinit var catagoryList: List<Category>
    fun getProductCategoryListInnerJoin(): LiveData<List<CategoryAndProductModel>> {
        return repository.getProductCategoryListInnerJoin()
    }

    fun getProductListFromCategoryIdInnerJoin(categoryID : Long): LiveData<List<CategoryAndProductModel>> {
        return repository.getProductListFromCategoryIdInnerJoin(categoryID)
    }

    suspend fun insertCategoryProductMapping(
        categoryId: Long,
        categoryName: String,
        productName: String
    ) {
        repository.getProductIdByInsertingInDataBase(categoryId, categoryName, productName)
    }

    fun deleteProduct(productId: Long) {
        repository.deleteByProductById(productId)
    }

    fun updateProductName(productId: Long, productName: String) {
        repository.updateProductName(productId, productName)
    }

    fun getPurchaseCountOfProductId(productId: Long): Int {
        return repository.getPurchaseCountOfProductId(productId)
    }

    suspend fun getAllCategories() {
        catagoryList = repository.getAllCategoriesSuspended()
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
    lateinit var repository: Repository
}