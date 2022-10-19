package com.example.hishab.repository

import android.app.Application
import com.example.hishab.db.dao.BudgetDao
import com.example.hishab.di.FooEntryPoint
import com.example.hishab.di.RepositoryEntryPoint
import com.example.hishab.interfaces.IPayloadHandler
import com.example.hishab.models.BudgetCategoryQuery
import com.example.hishab.models.BudgetGroupQueryModel
import com.example.hishab.models.MonthlySpentModel
import com.example.hishab.models.entities.Budget
import com.example.hishab.retrofit.RetrofitHelper
import com.example.hishab.retrofit.request.BudgetItemRequest
import com.example.hishab.retrofit.request.BudgetRequest
import com.example.hishab.retrofit.response.BudgetResponse
import com.example.hishab.utils.toIntList
import dagger.hilt.android.EntryPointAccessors
import io.reactivex.Flowable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BudgetRepository(application: Application) : IPayloadHandler {

    var categoryRepository: CategoryRepository
    var database = EntryPointAccessors.fromApplication(application, FooEntryPoint::class.java).database
    private var budgetDao: BudgetDao

    init {
        budgetDao = database.BudgetDao()
        var repositoryEntryPoint = EntryPointAccessors.fromApplication(application, RepositoryEntryPoint::class.java)
        categoryRepository = repositoryEntryPoint.categoryRepository
    }

    fun getBudgetList(month: Int, year: Int): Flowable<List<Budget>> {
        return budgetDao.getBudgetFlowable(month, year)
    }

    fun getCategoryBudgetList(month: Int, year: Int): List<BudgetCategoryQuery> {
        return budgetDao.getCategoryAndBudgetList(year, month)
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

    override suspend fun updateRemote() {
        var notSyncedBudgetData:List<BudgetGroupQueryModel> = budgetDao.getNotSyncedBudget()
        notSyncedBudgetData.forEach {
            var budgetIds = it.budgetIds.toIntList()
            var categoryIds = it.categoryIds.toIntList()
            var categoryNames = it.categoryNames.split(",")
            var categoryRemoteIds = it.categoryRemoteIds.toIntList()
            var budgetRemoteIds = it.budgetRemoteIds.toIntList()
            var budgets = it.budgets.toIntList()
            var budgetRequest = BudgetRequest(it.year,it.month)
            for(i in 0..budgetIds.size - 1){
                budgetRequest.addItem(BudgetItemRequest(
                    budgetRemoteIds[i],categoryIds[i],categoryNames[i],categoryRemoteIds[i],
                    budgetIds[i],budgets[i]
                ))
            }
            saveToRemote(budgetRequest)
        }
    }

    suspend fun saveToRemote(budgetRequest: BudgetRequest) {
        try{
            var response = RetrofitHelper.hishabApi.addOrUpdateBudget(budgetRequest)
            if(response.isSuccessful){
                if(response.body()!=null){
                    handleSuccess(response.body()!!)
                }
            }
        }catch (ex:Exception){

        }

    }

     private fun handleSuccess(response: BudgetResponse) {
         GlobalScope.launch {
             response.budgetItemResponses.forEach { response ->
                 categoryRepository.handleSuccess(response.categoryResponse)
                 budgetDao.updateRemoteId(response.budgetId,response.budgetLocalId.toLong())
             }
         }

    }
}