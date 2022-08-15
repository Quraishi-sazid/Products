package com.example.hishab.interfaces

import com.example.hishab.models.loginRequest
import com.example.hishab.retrofit.response.LoginResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IRetrofitService {
    @GET("home/api/test")
    fun getTestResult(): Call<String>

    @POST("home/api/login")
    fun login(@Body request: loginRequest ): Call<LoginResponse>
}