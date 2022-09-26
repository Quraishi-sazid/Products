package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {
    @Inject
    lateinit var repository: CategoryRepository

    fun getCategoryWithTotalProductMapped(): LiveData<List<CategoryProxy>> {
        return repository.getCategoryWithProductTableMap()
    }

    suspend fun insertCategory(category: Category) {
        repository.insertCategory(category)
    }

    suspend fun deleteCategoryById(deleteId: Long) {
        repository.deleteCategoryById(deleteId)
    }

    suspend fun updateCategory(updateCategory: Category, oldCategoryName:String?) {
        repository.updateCategory(updateCategory,oldCategoryName)
    }

    suspend fun getProductCountMappedWithCategoryId(id: Long):Int {
        return repository.getProductCountMappedWithCategoryId(id)
    }
}