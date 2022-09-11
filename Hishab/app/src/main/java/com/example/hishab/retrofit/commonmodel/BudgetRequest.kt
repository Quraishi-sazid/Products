package com.example.hishab.retrofit.commonmodel

import com.example.hishab.retrofit.JsonConverter

data class BudgetRequest  (
    var budgetId:Int,
    var budget:Int,
    var month:Int,
    var year:Int,
    var userId:Int,
    var categoryName:String
) : JsonConverter(){

}