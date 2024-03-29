package com.example.hishab.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.io.Serializable
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


data class ShoppingHistory(
    private var buyingId: Long,
    var remoteId:Long,
    private var totalCost: Int,
    private var totalItem: Int,
    private var day: Int,
    private var month: Int,
    private var year: Int,
    private var time: Long
):BaseObservable(),Serializable {

    @Bindable
    fun getBuyingId():Long
    {
        return buyingId
    }
    @Bindable
    fun getTotalCost():Int{
        return totalCost
    }
    @Bindable
    fun getTotalItem():Int{
        return totalItem
    }
    @Bindable
    fun getDay():Int{
        return day
    }
    @Bindable
    fun getMonth():Int{
        return month
    }
    @Bindable
    fun getYear():Int{
        return year
    }
    @Bindable
    fun getTime():Long{
        return time
    }
    @Bindable
    fun getDateFormatted():String{
        var monthString = "wrong"
        val dfs = DateFormatSymbols()
        val months: Array<String> = dfs.getMonths()
        if (month >= 1 && month <= 12) {
            monthString = months[month - 1]
        }
        return monthString.substring(0, 3)+" "+day
    }
    fun getTimeFormatted():String{
        var df = SimpleDateFormat("HH:mm aa");
        return  df.format(Date(time))
    }
}