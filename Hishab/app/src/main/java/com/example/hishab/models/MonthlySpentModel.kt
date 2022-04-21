package com.example.hishab.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.hishab.utils.Util
import java.io.Serializable

data class MonthlySpentModel(private val month:Int,private val year:Int,private val totalCost:Int):BaseObservable(),Serializable {

    @Bindable
    var budget:Int=0

    @Bindable
    fun getMonth():Int{
        return month
    }

    @Bindable
    fun getYear():Int{
        return year
    }
    @Bindable
    fun getTotalCost():Int{
        return totalCost
    }

    @Bindable
    fun getDisplayableMonth():String{
        return Util.getMonthForInt(month)
    }

    @Bindable
    fun getMessage():String{
        if(budget>totalCost)
            return "saved "+(budget-totalCost)+" tk"
        else
            return "exceeded" +(totalCost-budget)+" tk"
    }

}