package com.example.hishab.models

data class BudgetGroupQueryModel(
    var budgetIds: String,
    var categoryIds: String,
    var categoryNames: String,
    var categoryRemoteIds: String,
    var budgetRemoteIds: String,
    var budgets: String,
    var year: Int,
    var month: Int
) {


}