package com.example.hishab.workManager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.hilt.work.HiltWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hishab.HishabApplication
import com.example.hishab.di.RepositoryEntryPoint
import com.example.hishab.interfaces.IPayloadHandler
import com.example.hishab.utils.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltWorker
class ApiCallerWorker @AssistedInject constructor
    (@Assisted val context: Context, @Assisted workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    var handlerList = ArrayList<IPayloadHandler>()
    init {
        var repositoryEntryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            RepositoryEntryPoint::class.java
        )
        handlerList.add(repositoryEntryPoint.categoryRepository)
        handlerList.add(repositoryEntryPoint.productRepository)
        handlerList.add(repositoryEntryPoint.shoppingRepository)
        handlerList.add(repositoryEntryPoint.budgetRepository)
    }
    override fun doWork(): Result {
        /*val handler = Handler(Looper.getMainLooper())

        handler.post(Runnable {
            Toast.makeText(context,"show text",Toast.LENGTH_LONG).show()
            val notificationHelper = NotificationHelper(context)
            var nb = notificationHelper.getChannelNotification()
            notificationHelper.getManager().notify(1, nb.build())
        })*/
        runBlocking {
            retrieveAndSendToServer()
        }
        var output = Data.Builder().build()
        return Result.Success(output)
    }

    private suspend fun retrieveAndSendToServer(): Boolean = suspendCoroutine { continuation ->
        runBlocking {
            handlerList.forEach{it.updateRemote()}
        }
        continuation.resume(true)
    }
}