package com.example.hishab.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val notificationHelper = NotificationHelper(context)
            var isCalledAfterBoot =
                intent.action != null && (intent.action.equals("android.intent.action.BOOT_COMPLETED") || intent.action.equals(
                    "android.intent.action.QUICKBOOT_POWERON"
                ))
            if (isCalledAfterBoot) {
                var alarmHelper = AlarmHelper.Create<AlarmReceiver>(context, AlarmHelper.ReminderAlarmRequestCode, AlarmReceiver::class.java)
                var calendar = Calendar.getInstance()
                //calendar.add(Calendar.SECOND, 10)
                 calendar.set(Calendar.HOUR_OF_DAY, 20)
                if (calendar.get(Calendar.MILLISECOND) < Calendar.getInstance().get(Calendar.MILLISECOND))
                    calendar.add(Calendar.DATE, 1)
                alarmHelper.setRepeatingAlarm(calendar,120*1000)
                return;
            }
            var nb = notificationHelper.getChannelNotification()
            notificationHelper.getManager().notify(1, nb.build())
        }

    }


}