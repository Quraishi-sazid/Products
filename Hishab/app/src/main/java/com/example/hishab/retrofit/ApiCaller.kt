package com.example.hishab.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hishab.retrofit.response.CommonResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCaller<T>(private val responseFunction: Call<T>) {

    fun start(): LiveData<CommonResponse<T>> {
        var liveData = MutableLiveData<CommonResponse<T>>()
            responseFunction.enqueue(object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>
                ) {
                    liveData.value = CommonResponse(ApiCallStatus.SUCCESS, call, response, null)
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    liveData.value = CommonResponse(ApiCallStatus.ERROR, call, null, t)
                }
            })
        return liveData
    }
}

