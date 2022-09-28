package com.example.hishab.workManager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.hishab.di.RepositoryEntryPoint
import com.example.hishab.interfaces.IPayloadHandler
import com.example.hishab.models.PayLoadQuery
import com.example.hishab.repository.CategoryRepository
import com.example.hishab.repository.PayloadRepository
import com.example.hishab.repository.ProductRepository
import com.example.hishab.retrofit.ApiCallStatus
import com.example.hishab.retrofit.ApiURL
import com.example.hishab.retrofit.request.CategoryRequest
import com.example.hishab.retrofit.request.ProductRequest
import com.example.hishab.utils.Constant
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.coroutines.Continuation
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
    }
    override fun doWork(): Result {
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