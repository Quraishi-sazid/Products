package com.example.hishab.models.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.text.DateFormatSymbols
import java.util.*

data class PurchaseHistory(
    private val purchaseId: Int?,
    private val categoryName: String?,
    private val ItemName: String,
    private val cost: Int,
    private val descrip: String,
    private val day: Int,
    private val month: Int,
    private val year: Int
):BaseObservable()
{
    @Bindable
    fun getCategoryName():String?{
        return categoryName
    }

    @Bindable
    fun getItemName():String?{
        return ItemName
    }
    @Bindable
    fun getCost():Int?{
        return cost
    }
    @Bindable
    fun getDescipt():String?{
        return descrip
    }
    fun getDay():Int{
        return day
    }
    fun getMonth():Int{
        return month
    }
    fun getYear():Int{
        return year
    }
/*    @Bindable
    fun getPurchaseDateMili():Date{
        return purchaseDate
    }*/
    @Bindable
    fun getDateTimeString():String
    {
        var monthString = "wrong"
        val dfs = DateFormatSymbols()
        val months: Array<String> = dfs.getMonths()
        if (month >= 1 && month <= 12) {
            monthString = months[month-1]
        }
        return monthString.substring(0,3)+"\n"+day
    }
    fun getPurchaseId():Int?
    {
        return purchaseId;
    }

}