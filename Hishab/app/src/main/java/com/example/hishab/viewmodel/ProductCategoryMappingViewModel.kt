package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductCategoryMappingViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    fun getProductCategoryListInnerJoin(): LiveData<List<CategoryAndProductModel>> {
        return repository.getProductCategoryListInnerJoin()
    }

    suspend fun  insertCategoryProductMapping(categoryId:Long, categoryName:String, productName:String) {
         repository.getProductIdByInsertingInDataBase(categoryId,categoryName,productName)
    }

    fun deleteProduct(productId: Long) {
        repository.deleteByProductById(productId)
    }

    fun updateProductName(productId: Long, productName: String) {
        repository.updateProductName(productId,productName)
    }

    fun getPurchaseCountOfProductId(productId: Long):Int {
       return repository.getPurchaseCountOfProductId(productId)
    }

    @Inject
    lateinit var repository: Repository
}