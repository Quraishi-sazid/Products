package com.example.hishab.utils;

import com.example.hishab.interfaces.IRetrofitService
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

public class RetrofitHelper {
    companion object{
        val BASE_URL = "http://192.168.0.102:8080/"
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().create()
        var retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build()
        val hishabApi = retrofit.create(IRetrofitService::class.java)
    }

   /* RetrofitHelper.hishabApi.login(loginRequest("+8801781888888","sss","1234")).enqueue(object :
        Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if(response.isSuccessful)
            {
                var xx=5;
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            var ss= t.message
        }

    }*/

}
