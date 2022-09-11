package com.example.hishab.retrofit;

import com.google.gson.Gson

 open class JsonConverter() {
    fun convertToJson():String{
        return Gson().toJson(this)
    }
}
