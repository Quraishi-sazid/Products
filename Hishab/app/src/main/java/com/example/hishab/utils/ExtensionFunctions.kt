package com.example.hishab.utils

import com.google.gson.Gson

/*
inline fun <reified T> String.toTypedList():List<T>{
    var responseList = ArrayList<T>()
    var commaSeperatedList = this.split(",")
    commaSeperatedList.forEach {
        responseList.add(it as T)
    }
    return responseList
}*/

fun String.toIntList():List<Int>{
    var responseList = ArrayList<Int>()
    var commaSeperatedList = this.split(",")
    commaSeperatedList.forEach {
        responseList.add(Integer.parseInt(it))
    }
    return responseList
}
