package com.example.hishab

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.hishab.utils.AlarmHelper
import com.example.hishab.utils.AlarmReceiver
import com.example.hishab.utils.PreferenceHelper
import com.example.hishab.workManager.ApiCallerWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class HishabApplication : Application(), Configuration.Provider {

    companion object{
         fun setWorkManager(context: Context) {
            val constraints: Constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            var request =
                PeriodicWorkRequest.Builder(ApiCallerWorker::class.java,30,TimeUnit.MINUTES).setConstraints(constraints)
                    .build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork("RemoteUpdateWork", ExistingPeriodicWorkPolicy.KEEP, request)
        }
    }

    lateinit var reminderAlarmHelper: AlarmHelper<AlarmReceiver>
    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.sharedPreferences = applicationContext.getSharedPreferences("HishabPreference", Context.MODE_PRIVATE)
        reminderAlarmHelper = AlarmHelper.Create(applicationContext, AlarmHelper.ReminderAlarmRequestCode, AlarmReceiver::class.java)
        setAlarms()
        setWorkManager(applicationContext)
       // WorkManager.initialize(this,workManagerConfiguration)
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



    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}