package com.example.hishab.retrofit;

import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper
import com.google.gson.GsonBuilder;
import okhttp3.Interceptor
import okhttp3.OkHttpClient;
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper {
    companion object{
        val BASE_URL = "http://192.168.0.102:8080/"
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().create()
        val client = OkHttpClient.Builder()
        var retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(
            gson
        )).client(
            getClient()
        ).build()
        val hishabApi = retrofit.create(IRetrofitService::class.java)
        private fun getClient(): OkHttpClient{
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            var builder = OkHttpClient.Builder()

            builder.addInterceptor(loggingInterceptor)
            builder.addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request = chain.request().newBuilder().addHeader("Authorization","Bearer "+PreferenceHelper.get(Constant.jwt,null)).build()
                    return chain.proceed(request)
                }
            })
            builder.readTimeout(180, TimeUnit.SECONDS)
            builder.writeTimeout(180, TimeUnit.SECONDS)
            var client = builder.build()
            return client
        }
    }

}
