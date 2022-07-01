package com.example.hishab.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class BudgetCategoryQuery(@Bindable var budgetID :Long,@Bindable var categoryId:Long,@Bindable var categoryName:String,@Bindable var budget: Int) : BaseObservable() {
}