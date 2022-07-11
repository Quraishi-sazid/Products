package com.example.hishab.utils

import android.annotation.TargetApi;
import android.app.Notification
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import androidx.core.app.NotificationCompat
import com.example.hishab.R


class NotificationHelper(val context: Context) : ContextWrapper(context) {
    val channelID = "channelID"
    val channelName = "Channel Name"
    var msg = "Please input today's expense";

    private lateinit var mManager: NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        getManager()!!.createNotificationChannel(channel)
    }

    fun getManager(): NotificationManager {
        if (!this::mManager.isInitialized) {
            mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return mManager
    }

    fun getChannelNotification(): Notification.Builder {
        return Notification.Builder(applicationContext, channelID)
            .setContentTitle("Alarm!")
            .setContentText(msg)
            .setSmallIcon(R.drawable.ic_baseline_category_24)
    }
}