package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.BudgetCategoryQuery
import com.example.hishab.models.entities.Budget
import com.example.hishab.models.entities.Category
import com.example.hishab.repository.BudgetRepository
import com.example.hishab.repository.CategoryRepository
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import javax.inject.Inject

@HiltViewModel
class AddBudgetViewModel @Inject constructor (app: Application) : AndroidViewModel(app) {
    @Inject//hilt not instantiating.need to check
    lateinit var budgetRepository : BudgetRepository
    @Inject
    lateinit var categoryRepository: CategoryRepository

    var month = Util.getCurrentMonth()
    var year = Util.getCurrentYear()

    fun updateBudgetList(budgetList:List<Budget>){
        budgetRepository.updateBudgetList(budgetList);
    }

    fun getCategoryBudgetList(month:Int,year:Int):List<BudgetCategoryQuery> {
        return budgetRepository.getCategoryBudgetList(month,year)
    }

    suspend fun updateBudgetById(categoryId:Long,budget:Int,month: Int,year: Int){
        budgetRepository.updateBudgetById(categoryId,budget,month,year)
    }
    suspend fun insertBudget(budget:Budget){
        budgetRepository.insertBudget(budget)
    }
    fun getCategoryList() : List<Category>{
       return categoryRepository.getAllCategories();
    }

    suspend fun getBudgetFlowable() : Flowable<List<Budget>> {
        return budgetRepository.getBudgetList(month,year)
    }
}