package com.example.hishab.retrofit.request

import com.example.hishab.models.BudgetCategoryQuery

data class BudgetItemRequest(val budgetItemId:Int =-1) {
    var categoryRequest: CategoryRequest? = null
    var budget = 0
    var localId = 0

    constructor(budgetCategoryQuery: BudgetCategoryQuery):this(budgetCategoryQuery.budgetRemoteId!!){
        categoryRequest = CategoryRequest(budgetCategoryQuery.categoryRemoteId!!,budgetCategoryQuery.categoryName,budgetCategoryQuery.categoryId.toInt());
        budget = budgetCategoryQuery.budget
        localId = budgetCategoryQuery.budgetID.toInt()
    }
    constructor(budgetId:Int,categoryId:Int,categoryName:String,categoryRemoteId:Int,budgetLocalId:Int,budget:Int):this(budgetId){
        categoryRequest = CategoryRequest(categoryRemoteId,categoryName,categoryId)
        this.budget = budget
        localId = budgetLocalId
    }
}
