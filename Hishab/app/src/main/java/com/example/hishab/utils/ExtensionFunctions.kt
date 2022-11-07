package com.example.hishab.utils

import java.util.*

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

fun Date.getCalenderYear():Int {
    var date: Date // your date
    val cal = Calendar.getInstance()
    cal.time = this
    return cal[Calendar.YEAR]
}

fun Date.getCalenderMonth() : Int{
    var date: Date // your date
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.MONTH);
}

fun Date.getCalenderDay() : Int{
    var date: Date // your date
    val cal = Calendar.getInstance()
    cal.time = this
    return cal.get(Calendar.DAY_OF_MONTH);
}
