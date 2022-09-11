package com.example.hishab.workManager
import android.content.Context
import android.util.Log
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.hishab.models.PayLoadQuery
import com.example.hishab.repository.CategoryRepository
import com.example.hishab.repository.PayloadRepository
import com.example.hishab.retrofit.ApiCallStatus
import com.example.hishab.retrofit.ApiURL
import com.example.hishab.retrofit.request.CategoryRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ApiCallerWorker(val context:Context,workerParameters: WorkerParameters): Worker(context,workerParameters) {
    /*@Inject
    lateinit var payloadRepository: PayloadRepository
    @Inject
    lateinit var categoryRepository: CategoryRepository*/
    var payloadRepository: PayloadRepository
    var categoryRepository: CategoryRepository
    init {
        payloadRepository = PayloadRepository(context)
        categoryRepository = CategoryRepository(context)
    }



    override fun doWork(): Result {
        Log.v("override fun doWork(): Result ","override fun doWork(): Result ")
        runBlocking {
            retrieveAndSendToServer()
        }
        var output = Data.Builder().build()
        return Result.Success(output)
    }

    private suspend fun retrieveAndSendToServer():Boolean = suspendCoroutine { continuation ->
        var apiCallCounter = AtomicInteger(0)
        var data : List<PayLoadQuery>?
        runBlocking {
            data = payloadRepository.getPayLoadQuery()
        }
        if(data!= null && data!!.size != 0){
            data!!.forEach{ payloadQuery ->
                var payLoadIdList = payloadQuery.payloadIdList()
                when (payloadQuery.apiName){
                    ApiURL.CATEGORY_ADD_OR_UPDATE->{
                        var categoryRequestResponseList = payloadQuery.payloadList<CategoryRequest>()
                        GlobalScope.launch {
                            var response = categoryRepository.insertOrUpdateCategoryListInRemote(categoryRequestResponseList,payLoadIdList)
                            if(response.isSuccessful){
                                var count = 0
                                response?.body()?.forEach{
                                    categoryRepository.handleSuccess(it,payLoadIdList[count++])
                                }
                            }
                            resumeContinuationIfCompleted(apiCallCounter, data, continuation)
                        }
                    }
                }
            }
        }

    }

    private fun resumeContinuationIfCompleted(
        apiCallCounter: AtomicInteger,
        data: List<PayLoadQuery>?,
        continuation: Continuation<Boolean>
    ) {
        apiCallCounter.incrementAndGet()
        if (apiCallCounter.get() == data!!.size)
            continuation.resume(true)
    }
}