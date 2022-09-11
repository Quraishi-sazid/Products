package com.example.hishab.remotedatasource

import androidx.lifecycle.LiveData
import com.example.hishab.retrofit.ApiCaller
import com.example.hishab.retrofit.RetrofitHelper
import com.example.hishab.retrofit.request.CategoryRequest
import com.example.hishab.retrofit.response.CategoryResponse
import com.example.hishab.retrofit.response.CommonResponse
import com.google.gson.JsonObject
import io.reactivex.subjects.Subject

class CategoryRemoteDataSource() {
    fun insertOrUpdateCategoryInRemote(categoryRequest: CategoryRequest): Subject<CommonResponse<CategoryRequest, CategoryResponse>> {
        return ApiCaller<CategoryRequest, CategoryResponse>(
            RetrofitHelper.hishabApi
                .addOrUpdateCategory(categoryRequest), categoryRequest
        ).startObservingSubject();
    }

    /*fun insertOrUpdateCategoryListInRemote(categoryRequestResponseList: List<CategoryRequest>): Subject<CommonResponse<List<CategoryRequest>, List<CategoryResponse>>> {
        return ApiCaller<List<CategoryRequest>, List<CategoryResponse>>(
            RetrofitHelper.hishabApi
                .addOrUpdateCategoryList(categoryRequestResponseList)
        ).startObservingSubject()
    }*/
}