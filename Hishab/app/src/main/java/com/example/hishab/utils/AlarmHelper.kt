package com.example.hishab.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*
import kotlin.collections.HashMap

class AlarmHelper<T : BroadcastReceiver> private constructor(
    val context: Context,
    val requestCode: Int,
    val javaClass: Class<T>,
    val action: String = ""
) {
    val alarmManager: AlarmManager =
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
    val alarmIntent: Intent = Intent(context, javaClass)
    var isAlarmRegistered: Boolean = false
        get() = PendingIntent.getBroadcast(
            context,
            Constant.AlarmReceiverRequestCode,
            Intent(context.applicationContext, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) != null

    companion object {
        private var mp = HashMap<Int, Any>()
        val ReminderAlarmRequestCode = 2002
        fun <T : BroadcastReceiver> Create(
            context: Context,
            requestCode: Int,
            javaClass: Class<T>, action: String =""
        ): AlarmHelper<T> {
            var alarmHelper = AlarmHelper<T>(context, requestCode, javaClass, action)
            mp[requestCode] = alarmHelper
            return alarmHelper
        }

        fun <T : BroadcastReceiver> GetHelperObject(requestCode: Int): AlarmHelper<T>? {
            if (!mp.containsKey(requestCode))
                return null
            return mp[requestCode] as AlarmHelper<T>
        }
    }

    fun setRepeatingAlarm(firstAlarmCalender: Calendar, repeatingMilisec: Long) {
        alarmIntent.action = action
        var pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            firstAlarmCalender.timeInMillis,
            repeatingMilisec,
            pendingIntent
        )
    }

    fun cancelAlarm() {
        var pendingIntent =
            PendingIntent.getBroadcast(
                context,
                requestCode,
                alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        (context.getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager).cancel(
            pendingIntent
        )
        pendingIntent.cancel()
    }
}