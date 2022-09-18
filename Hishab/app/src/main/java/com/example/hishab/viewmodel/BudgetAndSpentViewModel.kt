package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.Budget
import com.example.hishab.repository.BudgetRepository
import com.example.hishab.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable

import javax.inject.Inject

@HiltViewModel
class BudgetAndSpentViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var budgetRepository: BudgetRepository
    @Inject
    lateinit var categoryRepository : CategoryRepository

    fun getBudgetAndCategoryList(month: Int, year: Int): Flowable<List<Budget>> {
        return budgetRepository.getBudgetList(month, year)
    }

    fun getCategorySpentsOfMonth(
        categoryIds: List<Long>,
        month: Int,
        year: Int
    ): List<CategoryCostModel> {
        return categoryRepository.getCategorySpentsOfMonth(categoryIds, month, year)
    }

    fun getCategoryIdAndNameMapping(): HashMap<Long, String> {
        var tempMap = HashMap<Long, String>()
        categoryRepository.getAllCategories().forEach { tempMap[it.categoryId] = it.getCategoryName() }
        return tempMap
    }

}