package com.example.hishab

import android.app.Application
import android.util.Log
import com.example.hishab.utils.AlarmHelper
import com.example.hishab.utils.AlarmReceiver
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class HishabApplication : Application() {
    lateinit var reminderAlarmHelper: AlarmHelper<AlarmReceiver>

    override fun onCreate() {
        super.onCreate()
        reminderAlarmHelper = AlarmHelper.Create(applicationContext, AlarmHelper.ReminderAlarmRequestCode, AlarmReceiver::class.java)
        setAlarms()
    }

    private fun setAlarms() {
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,20)
        if (calendar.get(Calendar.MILLISECOND) < Calendar.getInstance().get(Calendar.MILLISECOND))
            calendar.add(Calendar.DATE, 1)
        if(!reminderAlarmHelper.isAlarmRegistered){
            Log.v("AlarmManager","Setting up an alarm")
            reminderAlarmHelper.setRepeatingAlarm(calendar,86400*1000)
        }else{
            Log.v("AlarmManager","alarm already exists")
        }


    }

}