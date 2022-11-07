package com.example.hishab.repository

import android.app.Application
import android.content.Context
import com.example.hishab.di.RepositoryEntryPoint
import com.example.hishab.models.entities.*
import com.example.hishab.retrofit.RetrofitHelper
import com.example.hishab.utils.*
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class IHistoryRepository(application: Context) {
    var categoryRepository: CategoryRepository
    var productRepository: ProductRepository
    var budgetRepository: BudgetRepository
    var shoppingRepository: ShoppingRepository

    init {
        var repositoryEntryPoint = EntryPointAccessors.fromApplication(
            application.applicationContext,
            RepositoryEntryPoint::class.java
        )
        categoryRepository = repositoryEntryPoint.categoryRepository
        productRepository = repositoryEntryPoint.productRepository
        budgetRepository = repositoryEntryPoint.budgetRepository
        shoppingRepository = repositoryEntryPoint.shoppingRepository
    }
    suspend fun updateLocalDBFromRemote() {
        var userId = PreferenceHelper.get(Constant.User_Id,-1)
        var categoryIdCategoryNameMapping = HashMap<Int,String>()
        var categoryRemoteIdLocalIdMapping = HashMap<Int,Long>()
        var productRemoteIdLocalIdMapping = HashMap<Int,Long>()
        var shoppingRemoteIdLocalIdMapping = HashMap<Int,Long>()
        var categoryProductHistories = RetrofitHelper.hishabApi.getCategoryProductHistory(userId);
        categoryProductHistories.body()?.forEach {
            categoryIdCategoryNameMapping[it.categoryId] = it.categoryName
        }
         categoryIdCategoryNameMapping.forEach{
            var insertedCategoryId = categoryRepository.insertCategoryLocally(Category().apply { remoteId = it.key.toLong()
                    setCategoryName(it.value)
                    isSynced = true
            })
             categoryRemoteIdLocalIdMapping[it.key] = insertedCategoryId
        }
        categoryProductHistories.body()?.forEach{
            var insertedProductId = productRepository.insertProduct(Product().apply { remoteId = it.productId
                setProductName(it.productName)
                categoryId = categoryRemoteIdLocalIdMapping[it.categoryId]!!
                isSynced = true
            })
            productRemoteIdLocalIdMapping[it.productId] = insertedProductId
        }
        RetrofitHelper.hishabApi.getBudgetHistory(userId).body()?.forEach {
            runBlocking(Dispatchers.IO) {
                budgetRepository.insertBudget(Budget().apply {
                    if(categoryRemoteIdLocalIdMapping[it.categoryId]!=null)
                    {
                        remoteId = it.budgetId
                        categoryId = categoryRemoteIdLocalIdMapping[it.categoryId]!!
                        month = it.month
                        year = it.year
                        budget = it.budget
                        isSynced = true
                    }
                })
            }
        }
        var shoppingResponses = RetrofitHelper.hishabApi.getShoppingHistory(userId).body()
        var shoppingIdSet= HashSet<Int>()
        runBlocking(Dispatchers.IO) {
            shoppingResponses?.forEach {
                if(!shoppingIdSet.contains(it.shoppingId)){
                    var date = Date(it.date)
                    var dateId = shoppingRepository.getDateId(CustomDate(date.getCalenderYear(),date.getCalenderMonth() + 1,date.getCalenderDay()))
                    var insertedShoppingId = shoppingRepository.insertBuyingItem(Shopping(dateId,date.time,0,it.shoppingId.toLong(),true))
                    shoppingRemoteIdLocalIdMapping[it.shoppingId] = insertedShoppingId
                    shoppingIdSet.add(it.shoppingId)
                }
                shoppingRepository.insertPurchaseItem(PurchaseItem().apply {
                    setPurchaseId(0)
                    setCost(it.cost)
                    setDescription(it.description)
                    shoppingId = shoppingRemoteIdLocalIdMapping[it.shoppingId]!!
                    productId = productRemoteIdLocalIdMapping[it.productId]!!
                    isSynced = true
                    remoteId = it.shoppingItemId
                })
            }
        }

    }
}