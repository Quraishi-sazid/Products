package com.example.hishab.utils

import android.content.Context
import android.content.SharedPreferences

class Preferences private constructor(context: Context) {
    var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("HishabPreference", Context.MODE_PRIVATE)
    }

    companion object {
        var Instance: Preferences? = null
        fun getInstance(context: Context): Preferences {
            if (Instance == null)
                Instance = Preferences(context)
            return Instance!!
        }
    }

    fun save(key: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun save(key: String, value: Long) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    fun save(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getInt(key: String, defaultValue: Int = -1) :Int {
       return sharedPreferences.getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long = -1L) :Long {
        return sharedPreferences.getLong(key, defaultValue)
    }
    fun getString(key: String, defaultValue: String? = null) :String? {
        return sharedPreferences.getString(key,defaultValue)
    }
}