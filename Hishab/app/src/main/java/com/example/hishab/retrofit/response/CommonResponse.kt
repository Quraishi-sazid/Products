package com.example.hishab.retrofit.response

import com.example.hishab.retrofit.ApiCallStatus
import retrofit2.Call
import retrofit2.Response

data class CommonResponse<T>(val callStatus: ApiCallStatus,val call : Call<T>, val response: Response<T>?, val t:Throwable?) {

}