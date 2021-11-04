package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryCostViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var repository:Repository

    suspend fun GetCategoryCostModels():List<CategoryCostModel>
    {
        return repository.getTotalCostByCategoryFromDate()
    }
}