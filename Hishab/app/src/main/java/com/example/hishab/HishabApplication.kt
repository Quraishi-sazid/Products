package com.example.hishab

import android.app.Application
import com.example.hishab.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HishabApplication: Application() {
    override fun onCreate() {
        super.onCreate()
     //   var database = AppDatabase.getDatabase(applicationContext)
    }
}