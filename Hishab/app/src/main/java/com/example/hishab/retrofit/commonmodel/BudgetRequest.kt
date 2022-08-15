package com.example.hishab.retrofit.commonmodel

data class BudgetRequest(
    var budgetId:Int,
    var budget:Int,
    var month:Int,
    var year:Int,
    var userId:Int,
    var categoryName:String
) {

}