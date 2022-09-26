package com.example.hishab.retrofit

import com.example.hishab.retrofit.commonmodel.UserModel
import com.example.hishab.retrofit.request.CategoryRequest
import com.example.hishab.retrofit.request.LoginRequest
import com.example.hishab.retrofit.request.ProductRequest
import com.example.hishab.retrofit.request.ShoppingRequest
import com.example.hishab.retrofit.response.CategoryResponse
import com.example.hishab.retrofit.response.LoginResponse
import com.example.hishab.retrofit.response.ProductResponse
import com.example.hishab.retrofit.response.shoppingResponse

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IRetrofitService {
    @POST(value = ApiURL.LOGIN)
    fun login(@Body request: LoginRequest ): Call<LoginResponse>
    @POST(ApiURL.REGISTRATION)
    fun registration(@Body userModel: UserModel ): Call<UserModel>
    @POST(ApiURL.CATEGORY_ADD_OR_UPDATE)
    fun addOrUpdateCategory(@Body categoryRequest: CategoryRequest): Call<CategoryResponse>
    @POST(ApiURL.CATEGORYLIST_ADD_OR_UPDATE)
    suspend fun addOrUpdateCategoryList(@Body categoryRequestResponseList: List<CategoryRequest> ): Response<List<CategoryResponse>>
    @POST(ApiURL.PRODUCT_ADD_OR_UPDATE)
    suspend fun addOrUpdateProduct(@Body productRequest: ProductRequest): Response<ProductResponse>
    @POST(ApiURL.PRODUCTLIST_ADD_OR_UPDATE)
    suspend fun addOrUpdateProductList(@Body productRequestList: List<ProductRequest> ): Response<List<ProductResponse>>
    @POST(ApiURL.SHOPPING_ADD)
    suspend fun addShopping(@Body shoppingRequest: ShoppingRequest ): Response<shoppingResponse>
    @POST(ApiURL.SHOPPING_ADD_LIST)
    suspend fun addShoppingList(@Body shoppingRequestList: List<ShoppingRequest> ): Response<List<shoppingResponse>>
    @POST(ApiURL.SHOPPING_UPDATE)
    suspend fun updateShopping(@Body shoppingRequest: ShoppingRequest ): Response<shoppingResponse>
    @POST(ApiURL.SHOPPING_UPDATE_LIST)
    suspend fun updateShoppingList(@Body shoppingRequestList: List<ShoppingRequest> ): Response<List<shoppingResponse>>

}