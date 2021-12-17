package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(application: Application):AndroidViewModel(application) {
    @Inject
    lateinit var repository:Repository

    suspend fun getCategoryWithTotalProductMapped():List<CategoryProxy>{
        return  repository.getCategoryWithProductTableMap()
    }
}