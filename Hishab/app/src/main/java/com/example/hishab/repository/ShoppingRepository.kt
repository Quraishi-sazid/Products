package com.example.hishab.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.hishab.db.dao.*
import com.example.hishab.di.FooEntryPoint
import com.example.hishab.di.RepositoryEntryPoint
import com.example.hishab.interfaces.IPayloadHandler
import com.example.hishab.models.*
import com.example.hishab.models.entities.*
import com.example.hishab.retrofit.RetrofitHelper
import com.example.hishab.retrofit.request.ShoppingItemRequest
import com.example.hishab.retrofit.request.ShoppingRequest
import com.example.hishab.retrofit.response.shoppingResponse
import dagger.hilt.android.EntryPointAccessors


class ShoppingRepository(application: Application) : IPayloadHandler {
    private var purchaseDao: PurchaseDao
    private var purchaseHistoryDao: PurchaseHistoryDao
    private var shoppingDao: ShoppingDao
    private var customDateDao: DateDao
    var database =
        EntryPointAccessors.fromApplication(application, FooEntryPoint::class.java).database
    var categoryRepository: CategoryRepository
    var productRepository: ProductRepository

    init {
        purchaseDao = database.PurchaseDao()
        purchaseHistoryDao = database.PurchaseShoppingCategoryDao()
        shoppingDao = database.shoppingDao()
        customDateDao = database.customDao()
        var repositoryEntryPoint = EntryPointAccessors.fromApplication(
            application.applicationContext,
            RepositoryEntryPoint::class.java
        )
        categoryRepository = repositoryEntryPoint.categoryRepository
        productRepository = repositoryEntryPoint.productRepository
    }

    suspend fun insertPurchaseItem(purchaseItem: PurchaseItem) {
        return purchaseDao.insertAll(purchaseItem)
    }

    fun getPurchaseHistory(
        lastPurchaseId: Long,
        loadSize: Int
    ): List<PurchaseHistory>//live data needed
    {
        return purchaseHistoryDao.getPurchaseHistory(lastPurchaseId, loadSize)
    }

    suspend fun getAllTimeTotalCostByCategory(): List<CategoryCostModel>//live data needed
    {
        return purchaseDao.getAllTimeTotalCostByCategory()
    }

    suspend fun deletePurchaseHistory(position: Long) {
        purchaseDao.deleteByPurchaseId(position)
    }

    fun getDetailsOfCategory(
        categoryId: Int,
        lastPurchaseId: Long,
        loadSize: Int
    ): List<PurchaseHistory> {
        return purchaseHistoryDao.getCategoryDetails(categoryId, lastPurchaseId, loadSize)
    }

    fun getDetailsOfCategoryOfMonthAndYear(
        lastPurchaseId: Long, limit: Int,
        categoryId: Int,
        month: Int,
        year: Int
    ): List<PurchaseHistory> {
        return purchaseHistoryDao.getCategoryDetailsOfMonth(
            lastPurchaseId,
            limit,
            categoryId,
            month,
            year
        )
    }

    suspend fun getDateId(customDate: CustomDate): Long {
        var qDate = customDateDao.getDateId(
            customDate.getDay(),
            customDate.getMonth(),
            customDate.getYear()
        )
        if (qDate == null) {
            customDateDao.insert(customDate)//need to take return value from here
            //room doesn't support object field in query. so primitive types are sent
            qDate = customDateDao.getDateId(
                customDate.getDay(),
                customDate.getMonth(),
                customDate.getYear()
            )
        }
        return qDate.getDateId()
    }

    suspend fun insertBuyingItem(shopping: Shopping): Long {
        return shoppingDao.insert(shopping)
    }

    suspend fun getPurchaseHistoryFromBuyingId(buyingId: Long): List<PurchaseHistory> {
        return purchaseHistoryDao.getPurchaseHistoryByBuyingId(buyingId)
    }

    fun getBuyingHistory(): LiveData<List<ShoppingHistory>> {
        return shoppingDao.getBuyingHistory()
    }

    suspend fun updatePurchaseItem(purchaseItem: PurchaseItem) {
        purchaseItem.isSynced = false
        purchaseDao.update(purchaseItem)
    }

    suspend fun updateDateId(dateId: Long, buyingId: Long) {
        shoppingDao.updateDateId(dateId, buyingId);
    }

    suspend fun getCategoryCostFromMonthAndYear(month: Int, year: Int): List<CategoryCostModel> {
        return purchaseDao.getCategoryCostFromMonthAndYear(month, year)
    }

    suspend fun updateTimeForShopping(milisec: Long, shoppingId: Long) {
        shoppingDao.updateShoppingTime(milisec, shoppingId)
    }

    override suspend fun updateRemote() {
        var idCategoryMap = HashMap<Long, Category>()
        var idProductMap = HashMap<Long, Product>()
        var shoppingIdPurchseItemsMap = HashMap<Long, ArrayList<PurchaseItem>>()
        var newShoppingRequestList = ArrayList<ShoppingRequest>()
        var updatebaleShoppingRequestList = ArrayList<ShoppingRequest>()
        categoryRepository.getAllCategoriesSuspended().forEach {
            idCategoryMap[it.categoryId] = it
        }
        productRepository.getAllProductsSuspended().forEach {
            idProductMap[it.productId] = it
        }
        purchaseDao.getAllUnUpdatedPurchaseItem().forEach {
            if (!shoppingIdPurchseItemsMap.containsKey(it.shoppingId)) {
                shoppingIdPurchseItemsMap[it.shoppingId] = ArrayList<PurchaseItem>()
            }
            shoppingIdPurchseItemsMap[it.shoppingId]?.add(it)
        }
        shoppingIdPurchseItemsMap.forEach {
            var shoppingId = it.key
            var shopping = shoppingDao.getShoppingById(shoppingId)
            var shoppingItemRequestList = ArrayList<ShoppingItemRequest>()
            it.value.forEach { purchaseItem ->
                var product = idProductMap[purchaseItem.productId]
                var category = idCategoryMap[product!!.categoryId]
                shoppingItemRequestList.add(
                    ShoppingItemRequest(
                        purchaseItem,
                        product!!,
                        category!!
                    )
                )
            }
            if (shopping.remoteId == -1L) {
                newShoppingRequestList.add(ShoppingRequest(shopping, shoppingItemRequestList))
            } else
                updatebaleShoppingRequestList.add(
                    ShoppingRequest(
                        shopping,
                        shoppingItemRequestList
                    )
                )
        }
        if (newShoppingRequestList.size > 0)
            saveShoppingRequestListToRemote(newShoppingRequestList, false)
        else
            saveShoppingRequestListToRemote(updatebaleShoppingRequestList, true)
    }

    suspend fun saveShoppingRequestToRemote(shoppingRequest: ShoppingRequest, isUpdating: Boolean) {
        try {
            var shoppingResponse: shoppingResponse? = null
            if (!isUpdating) {
                shoppingResponse = RetrofitHelper.hishabApi.addShopping(shoppingRequest).body()
            } else {
                shoppingResponse = RetrofitHelper.hishabApi.updateShopping(shoppingRequest).body()
            }
            handleSuccessApiCall(shoppingResponse)
        } catch (exception: Exception) {

        }
    }

    suspend fun saveShoppingRequestListToRemote(
        shoppingRequestList: List<ShoppingRequest>,
        isUpdating: Boolean
    ) {
        try {
            var shoppingResponseList: List<shoppingResponse>? = null
            if (!isUpdating) {
                shoppingResponseList =
                    RetrofitHelper.hishabApi.addShoppingList(shoppingRequestList).body()
            } else {
                shoppingResponseList =
                    RetrofitHelper.hishabApi.updateShoppingList(shoppingRequestList).body()
            }
            shoppingResponseList?.forEach {
                handleSuccessApiCall(it)
            }

        } catch (exception: Exception) {

        }
    }


    private suspend fun handleSuccessApiCall(shoppingResponse: shoppingResponse?) {
        if (shoppingResponse == null)
            return;
        shoppingDao.updateShoppingRemoteId(shoppingResponse.shoppingId, shoppingResponse.localId)
        shoppingResponse.itemResponses?.forEach { shoppingItemResponse ->
            purchaseDao.updateShoppingItemRemoteId(
                shoppingItemResponse.shoppingItemId,
                shoppingItemResponse.localId
            )
            productRepository.handleSuccess(shoppingItemResponse.productResponse)
        }
    }

}