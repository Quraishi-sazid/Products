package com.example.hishab.repository

import android.app.Application
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.BudgetDao
import com.example.hishab.models.BudgetCategoryQuery
import com.example.hishab.models.MonthlySpentModel
import com.example.hishab.models.entities.Budget
import io.reactivex.Flowable
import javax.inject.Inject

class BudgetRepository(application: Application)  {

    var database = AppDatabase.getDatabase(application)
    private var budgetDao: BudgetDao
    init {
        budgetDao = database.BudgetDao()
    }
    fun getBudgetList(month: Int, year: Int): Flowable<List<Budget>> {
        return budgetDao.getBudgetFlowable(month, year)
    }


    fun getCategoryBudgetList(month:Int,year:Int): List<BudgetCategoryQuery> {
        var list = budgetDao.getCategoryAndBudgetList(year,month)
        return list
    }

    suspend fun updateBudgetById(categoryId: Long, budget: Int, month: Int, year: Int){
        budgetDao.updateBudgetById(categoryId,budget,month,year)
    }

    fun insertBudget(budget: Budget) {
        budgetDao.insert(budget)
    }

    fun getBudgetFromMonthAndYear(year: Int, month: Int): Int {
        return budgetDao.getBudgetFromMonthAndYear(year, month)
    }

    fun getPreviousMonthsBudgetAndSpentHistory(
        year: Int,
        month: Int
    ): Flowable<List<MonthlySpentModel>> {
        return budgetDao.getPreviousBudgetSpentList(month, year)
    }

    fun updateBudgetList(budgetList: List<Budget>) {
        budgetDao.updateBudgetList(budgetList)
    }


}