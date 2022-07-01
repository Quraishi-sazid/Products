package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.BudgetCategoryQuery
import com.example.hishab.models.entities.Budget
import com.example.hishab.models.entities.Category
import com.example.hishab.repository.Repository
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import javax.inject.Inject

@HiltViewModel
class AddBudgetViewModel @Inject constructor (app: Application) : AndroidViewModel(app) {
 /*   @Inject//hilt not instantiating.need to check
    lateinit*/ var repository= Repository(app)
    var month = Util.getCurrentMonth()
    var year = Util.getCurrentYear()

    fun updateBudgetList(budgetList:List<Budget>){
        repository.updateBudgetList(budgetList);
    }

    fun getCategoryBudgetList(month:Int,year:Int):List<BudgetCategoryQuery> {
        return repository.getCategoryBudgetList(month,year)
    }

    suspend fun updateBudgetById(categoryId:Long,budget:Int,month: Int,year: Int){
        repository.updateBudgetById(categoryId,budget,month,year)
    }
    suspend fun insertBudget(budget:Budget){
        repository.insertBudget(budget)
    }
    fun getCategoryList() : List<Category>{
       return repository.getAllCategories();
    }

    suspend fun getBudgetFlowable() : Flowable<List<Budget>> {
        return repository.getBudgetList(month,year)
    }
}