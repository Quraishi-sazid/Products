package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.Budget
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable

import javax.inject.Inject

@HiltViewModel
class BudgetAndSpentViewModel @Inject constructor(app:Application):AndroidViewModel(app) {
    @Inject
    lateinit var repository: Repository

    fun getBudgetAndCategoryList(month:Int,year:Int): Flowable<List<Budget>>
    {
        return repository.getBudgetList(month,year)
    }

    fun getCategorySpentsOfMonth(categoryId: Long): String? {
        return repository.getCategoryNameFromCategoryId(categoryId)
    }
    fun getCategorySpentsOfMonth(categoryIds: List<Long>, month:Int, year:Int): List<CategoryCostModel> {
        return repository.getCategorySpentsOfMonth(categoryIds,month,year)
    }

}