package com.example.hishab.utils

import com.google.gson.Gson

inline fun <reified T> String.toTypedList():List<T>{
    var responseList = ArrayList<T>()
    var commaSeperatedList = this.split(",")
    commaSeperatedList.forEach {
        responseList.add(Gson().fromJson(this,T::class.java))
    }
    return responseList
}