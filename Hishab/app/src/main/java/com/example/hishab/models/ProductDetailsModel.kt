package com.example.hishab.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.text.SimpleDateFormat
import java.util.*

data class ProductDetailsModel(
    @Bindable var productId: Long, @Bindable var shoppingId: Long,
    @Bindable var productName: String, @Bindable var cost:Int, var time: Long, var day: Int, var month: Int, var year: Int
) : BaseObservable() {
    @Bindable
    fun getReadableDate():String{
        var df = SimpleDateFormat("HH:mm aa");
        return  df.format(Date(time))
    }

    @Bindable
    fun getReadableTime():String{
        var df = SimpleDateFormat("yyyy-MM-dd");
        return  df.format(Date(time))
    }
}