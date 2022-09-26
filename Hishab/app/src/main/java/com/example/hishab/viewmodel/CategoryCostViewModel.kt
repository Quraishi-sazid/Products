package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryCostViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var shoppingRepository:ShoppingRepository

    suspend fun getAllTimeTotalCostByCategory():List<CategoryCostModel>
    {
        return shoppingRepository.getAllTimeTotalCostByCategory()
    }

    suspend fun getCategoryCostFromMonthAndYear(month: Int, year: Int): List<CategoryCostModel> {
        return shoppingRepository.getCategoryCostFromMonthAndYear(month,year)
    }
}