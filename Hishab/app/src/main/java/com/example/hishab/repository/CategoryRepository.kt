package com.example.hishab.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.BudgetDao
import com.example.hishab.db.dao.CategoryDao
import com.example.hishab.db.dao.PayLoadDao
import com.example.hishab.db.dao.ProductDao
import com.example.hishab.di.FooEntryPoint
import com.example.hishab.interfaces.IPayloadHandler
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.remotedatasource.CategoryRemoteDataSource
import com.example.hishab.retrofit.ApiCallStatus
import com.example.hishab.retrofit.RetrofitHelper
import com.example.hishab.retrofit.request.CategoryRequest
import com.example.hishab.retrofit.response.CategoryResponse
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class CategoryRepository(application: Context) : IPayloadHandler {
    var database = EntryPointAccessors.fromApplication(application, FooEntryPoint::class.java).database
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

    suspend fun insertCategoryLocally(category: Category): Long {
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
                    /* GlobalScope.launch {
                         saveDataToPayloadDb(it!!.request!!.localId.toLong())
                     }*/
                }
            }
        }
        subject.doOnComplete {
            disposable.dispose()
        }
    }

    suspend fun handleSuccess(categoryResponse: CategoryResponse?) {
        if (categoryResponse != null) {
            categoryDao.updateRemoteId(
                categoryResponse.categoryId.toLong(),
                categoryResponse.localId.toLong()
            )
        }
    }

    suspend fun updateCategory(updateCategory: Category, oldCategoryName: String?) {
        updateCategory.isSynced = false
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

    suspend fun insertOrUpdateCategoryListInRemote(
        categoryRequestResponseList: List<CategoryRequest>
    ): Response<List<CategoryResponse>> {
        return RetrofitHelper.hishabApi.addOrUpdateCategoryList(categoryRequestResponseList);
    }

    suspend fun getCategoryFromName(categoryName: String): Category {
        return categoryDao.getCategoryFromName(categoryName)
    }

    suspend fun getCategoryById(categoryId: Long): Category {
        return categoryDao.getCategoryById(categoryId)
    }

    override suspend fun updateRemote() {
        var categoryRequestList = ArrayList<CategoryRequest>()
        categoryDao.getUnupdatedCategories().forEach { category ->
            categoryRequestList.add(CategoryRequest(category))
        }
        try {
            var response = insertOrUpdateCategoryListInRemote(categoryRequestList)
            if (response.body() != null) {
                response.body()!!.forEach { handleSuccess(it) }
            }
        } catch (ex: Exception) {
        }
    }

    fun getCategorySpentsOfMonth(
        categoryIdList: List<Long>,
        month: Int,
        year: Int
    ): List<CategoryCostModel> {
        return categoryDao.getCategorySpents(categoryIdList, month, year)
    }

    fun getAllCategories():List<Category>{
        return categoryDao.getAllCategory()
    }

    suspend fun getAllCategoriesSuspended():List<Category>{
        return categoryDao.getAllCategory()
    }

}