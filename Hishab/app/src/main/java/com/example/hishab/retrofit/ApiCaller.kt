package com.example.hishab.retrofit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hishab.retrofit.response.CommonResponse
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCaller<Req, T>(private val responseFunction: Call<T>, var param: Req? = null) {
    fun start(): LiveData<CommonResponse<Req, T>> {
        var liveData = MutableLiveData<CommonResponse<Req, T>>()
        responseFunction.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                var response = CommonResponse(param, ApiCallStatus.SUCCESS, call, response, null)
                liveData.value = response
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                liveData.value = CommonResponse(param, ApiCallStatus.ERROR, call, null, t)
            }
        })
        return liveData
    }

    fun startObservingSubject(): Subject<CommonResponse<Req,T>> {
        var subject = PublishSubject.create<CommonResponse<Req,T>>()
        responseFunction.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                if (response.isSuccessful) {
                    var response = CommonResponse(param, ApiCallStatus.SUCCESS, call, response, null)
                    subject.onNext(response)
                    subject.onComplete()
                } else {
                    var response = CommonResponse(param, ApiCallStatus.ERROR, call, null, null)
                    subject.onNext(response)
                    subject.onComplete()
                }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                subject.onNext(CommonResponse(param, ApiCallStatus.ERROR, call, null, t))
            }
        })
        return subject
    }
}

