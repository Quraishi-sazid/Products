package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.repository.ShoppingRepository

class CategoryCostViewModel(app: Application) : AndroidViewModel(app) {
    private val repository= ShoppingRepository(app)

    suspend fun GetCategoryCostModels():List<CategoryCostModel>
    {
        return repository.getTotalCostByCategoryFromDate()
    }
}