package com.example.hishab.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.*
import com.example.hishab.models.*
import com.example.hishab.models.entities.*
import io.reactivex.Flowable


class Repository(application: Application) {
    private var purchaseDao: PurchaseDao
    private var purchaseHistoryDao: PurchaseHistoryDao
    private var shoppingDao: ShoppingDao
    private var customDateDao: DateDao
    var database = AppDatabase.getDatabase(application)
    //var database = EntryPoints.get(application, FooEntryPoint::class.java).database

    init {
        purchaseDao = database.PurchaseDao()
        purchaseHistoryDao = database.PurchaseShoppingCategoryDao()
        shoppingDao = database.shoppingDao()
        customDateDao = database.customDao()
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
        purchaseDao.update(purchaseItem)
    }

    suspend fun updateDateId(dateId: Long, buyingId: Long) {
        shoppingDao.updateDateId(dateId, buyingId);
    }

    suspend fun getCategoryCostFromMonthAndYear(month: Int, year: Int): List<CategoryCostModel> {
        return purchaseDao.getCategoryCostFromMonthAndYear(month, year)
    }

    suspend fun updateTimeForShopping(milisec: Long,shoppingId:Long) {
        shoppingDao.updateShoppingTime(milisec,shoppingId)
    }

}