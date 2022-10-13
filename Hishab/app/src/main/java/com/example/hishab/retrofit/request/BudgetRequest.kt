package com.example.hishab.retrofit.request

import com.example.hishab.models.BudgetCategoryQuery
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper

data class BudgetRequest(val year:Int,val month:Int) {
    fun addItem(budgetItemRequest: BudgetItemRequest) {
        budgetItemRequests.add(budgetItemRequest)
    }

    val userId:Int
    val budgetItemRequests = ArrayList<BudgetItemRequest>()
    init {
        userId = PreferenceHelper.get(Constant.User_Id,0)
    }
    constructor(year:Int,month:Int,budgetCategoryQueryList:List<BudgetCategoryQuery>):this(year,month){
        budgetCategoryQueryList.forEach {
            budgetItemRequests.add(BudgetItemRequest(it))
        }
    }

}