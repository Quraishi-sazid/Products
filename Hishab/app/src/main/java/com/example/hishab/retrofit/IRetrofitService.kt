package com.example.hishab.retrofit

import com.example.hishab.retrofit.commonmodel.UserModel
import com.example.hishab.retrofit.request.CategoryRequest
import com.example.hishab.retrofit.request.LoginRequest
import com.example.hishab.retrofit.response.CategoryResponse
import com.example.hishab.retrofit.response.LoginResponse
import com.google.gson.JsonObject

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface IRetrofitService {
    @GET("home/api/test")
    fun getTestResult(): Call<String>

    @POST(value = ApiURL.LOGIN)
    fun login(@Body request: LoginRequest ): Call<LoginResponse>
    @POST(ApiURL.REGISTRATION)
    fun registration(@Body userModel: UserModel ): Call<UserModel>
    @POST(ApiURL.CATEGORY_ADD_OR_UPDATE)
    fun addOrUpdateCategory(@Body categoryRequest: CategoryRequest): Call<CategoryResponse>
    @POST(ApiURL.CATEGORYLIST_ADD_OR_UPDATE)
    suspend fun addOrUpdateCategoryList(@Body categoryRequestResponseList: List<CategoryRequest> ): Response<List<CategoryResponse>>

}