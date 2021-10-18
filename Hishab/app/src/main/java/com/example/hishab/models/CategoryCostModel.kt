package com.example.hishab.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class CategoryCostModel(private val Cost:Int,
                             private val CategoryId:Int,
                             private val CategoryName:String): BaseObservable()
{
@Bindable
fun getCategoryName():String
{
    return  CategoryName
}


    @Bindable
    fun getCost():Int
    {
        return  Cost
    }


}