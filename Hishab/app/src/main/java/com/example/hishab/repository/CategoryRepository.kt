package com.example.hishab.repository

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.BudgetDao
import com.example.hishab.db.dao.CategoryDao
import com.example.hishab.db.dao.PayLoadDao
import com.example.hishab.db.dao.ProductDao
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PayLoad
import com.example.hishab.remotedatasource.CategoryRemoteDataSource
import com.example.hishab.retrofit.ApiCallStatus
import com.example.hishab.retrofit.ApiURL
import com.example.hishab.retrofit.RetrofitHelper
import com.example.hishab.retrofit.request.CategoryRequest
import com.example.hishab.retrofit.response.CategoryResponse
import com.example.hishab.retrofit.response.CommonResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class CategoryRepository(application: Context) {
    //var database = EntryPoints.get(application, FooEntryPoint::class.java).database
    var database = AppDatabase.getDatabase(application)
    var categoryRemoteDataSource = CategoryRemoteDataSource()
    private var categoryDao: CategoryDao
    private var budgetDao: BudgetDao
    private var productDao: ProductDao
    private var payLoadDao: PayLoadDao

    init {
        categoryDao = database.CategoryDao()
        budgetDao = database.BudgetDao()
        productDao = database.ProductDao()
        payLoadDao = database.paylodDao()
    }

    suspend fun insertCategoryLocally(category: Category):Long{
         return categoryDao.insertCategory(category)
    }

    suspend fun insertCategory(category: Category, oldCategoryName: String? = null) {
        category.categoryId = insertCategoryLocally(category)
        handleRemote(category, oldCategoryName)
    }

    private fun handleRemote(
        category: Category,
        oldCategoryName: String?
    ) {
        var subject = categoryRemoteDataSource.insertOrUpdateCategoryInRemote(
            CategoryRequest(
                category.remoteId.toInt(),
                category.getCategoryName(),
                oldCategoryName,
                category.categoryId.toInt()
            )
        )
        var disposable = subject.subscribe {
            when (it.callStatus) {
                ApiCallStatus.SUCCESS -> {
                    GlobalScope.launch {
                        handleSuccess(it.response?.body())
                    }
                }
                ApiCallStatus.ERROR -> {
                    GlobalScope.launch {
                        var payLoadId = payLoadDao.insertPayload(
                            PayLoad(
                                0,
                                ApiURL.CATEGORY_ADD_OR_UPDATE,
                                it.request!!.convertToJson()
                            )
                        )
                        categoryDao.updateLocalIdAndPayloadId(
                            -1,
                            payLoadId,
                            it.request.localId.toLong()
                        )
                    }
                }
            }
        }
        subject.doOnComplete {
            disposable.dispose()
        }
    }

    suspend fun handleSuccess(categoryResponse: CategoryResponse?, payLoadId: Int = -1) {
        if (categoryResponse != null) {
                categoryDao.updateLocalIdAndPayloadId(
                    categoryResponse.categoryId.toLong(),
                    -1L,
                    categoryResponse.localId.toLong()
                )
                if (payLoadId != -1) {
                    payLoadDao.deleteById(payLoadId.toLong())
                }
        }
    }

    suspend fun updateCategory(updateCategory: Category, oldCategoryName: String?) {
        categoryDao.updateCategory(updateCategory)
        handleRemote(updateCategory, oldCategoryName)
    }

    fun getCategoryWithProductTableMap(): LiveData<List<CategoryProxy>> {
        return categoryDao.getCategoryWithTotalProductMapped();
    }

    suspend fun deleteCategoryById(deleteId: Long) {
        categoryDao.deleteCategoryById(deleteId)
        budgetDao.deleteByCategoryId(deleteId)
    }

    suspend fun getProductCountMappedWithCategoryId(id: Long): Int {
        return productDao.getProductCountMappedWithCategoryId(id)
    }

    suspend fun insertOrUpdateCategoryListInRemote (
        categoryRequestResponseList: List<CategoryRequest>): Response<List<CategoryResponse>> {
        return RetrofitHelper.hishabApi.addOrUpdateCategoryList(categoryRequestResponseList);
    }

    suspend fun getCategoryFromName(categoryName: String): Category {
        return categoryDao.getCategoryFromName(categoryName)
    }

    suspend fun getCategoryById(categoryId: Long): Category {
        return categoryDao.getCategoryById(categoryId)
    }

}