package com.example.hishab.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper {
    companion object {
        lateinit var sharedPreferences: SharedPreferences
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
        fun get(key: String, defaultValue: Int = -1) :Int {
            return sharedPreferences.getInt(key, defaultValue)
        }
        fun get(key: String, defaultValue: Long = -1L) :Long {
            return sharedPreferences.getLong(key, defaultValue)
        }
        fun get(key: String, defaultValue: String? = null) :String? {
            return sharedPreferences.getString(key,defaultValue)
        }
    }


}