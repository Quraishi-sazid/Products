package com.example.hishab.models

import com.google.gson.Gson
import kotlin.reflect.typeOf

data class PayLoadQuery(val payloadIds: String, val apiName: String, val allPayload: String) {
    inline fun <reified T> payloadList(): List<T> {
        var returnList = ArrayList<T>()
        allPayload.split("#4567#").map {
            var item = Gson().fromJson(it, T::class.java)
            returnList.add(item)
        }
        return returnList
    }

     fun  payloadIdList(): List<Int> {
        var returnList = ArrayList<Int>()
         payloadIds.split("#4567#").map {
            var item = Gson().fromJson(it, Int::class.java)
            returnList.add(item)
        }
        return returnList
    }
}