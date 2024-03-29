package com.example.hishab.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.io.Serializable

data class CategoryCostModel(private val Cost:Int,
                             private val CategoryId:Long,
                             private val CategoryName:String): BaseObservable(),Serializable
{
@Bindable
fun getCategoryName():String
{
    return  CategoryName
}
    @Bindable
    fun getCategoryId():Long
    {
        return  CategoryId
    }

    @Bindable
    fun getCost():Int
    {
        return  Cost
    }


}