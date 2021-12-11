package com.example.hishab.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.hishab.db.dao.*
import com.example.hishab.di.FooEntryPoint
import com.example.hishab.models.BuyingHistory
//import com.example.hishab.di.FooEntryPoint
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.*
import dagger.hilt.EntryPoints


class Repository(application: Application) {
    private var categoryDao: CategoryDao
    private var shoppingDao: ShoppingDao
    private var purchaseDao: PurchaseDao
    private var purchaseShoppingCategoryDao: PurchaseShoppingCategoryDao
    private var buyingDao: BuyingDao
    private var customDateDao: DateDao
    var database = EntryPoints.get(application, FooEntryPoint::class.java).database

    init {
        categoryDao = database.CategoryDao()
        shoppingDao = database.ShoppingDao()
        purchaseDao = database.PurchaseDao()
        purchaseShoppingCategoryDao = database.PurchaseShoppingCategoryDao()
        buyingDao = database.BuyingDao()
        customDateDao = database.customDao()
    }

    suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertAll(category)
    }

    suspend fun getCategoryIdFromName(name: String): Category {
        return categoryDao.getCategoryIdFromName(name);
    }

    suspend fun insertShopping(product: Product): Long {
        return shoppingDao.insert(product)
    }

    suspend fun getShoppingItemFromNameAndCId(itemName: String, categoryId: Long): Product {
        return shoppingDao.getShoppingItemFromItemNameAndCategoryId(itemName, categoryId);
    }

    suspend fun insertPurchaseItem(purchaseItem: PurchaseItem) {
        return purchaseDao.insertAll(purchaseItem)
    }

    suspend fun getPurchaseItems(): List<PurchaseItem> {
        return purchaseDao.getAllPurchase()
    }

    suspend fun getPurchaseHistory(): LiveData<List<PurchaseHistory>>//live data needed
    {
        return purchaseShoppingCategoryDao.getPurchaseHistory()
    }

    suspend fun getTotalCostByCategoryFromDate(): List<CategoryCostModel>//live data needed
    {
        return purchaseDao.getTotalCostByCategoryFromDate(0,0,0)
    }

    suspend fun deletePurchaseHistory(position: Long) {
        purchaseDao.deleteByPurchaseId(position)
    }

    suspend fun updatePurchaseItem(
        product: Product,
        purchaseItem: PurchaseItem
    ) {
        //purchaseDao.update(product.productId,purchaseItem.getPurchaseId(),purchaseItem.getCost(),purchaseItem.day,purchaseItem.month,purchaseItem.year);
    }

    suspend fun getdetailsOfCategoryfromDate(
        categoryId: Int,
        dateModel: CustomDate
    ): LiveData<List<PurchaseHistory>> {
        return purchaseShoppingCategoryDao.getDetailsOfCategoryFromDate(
            categoryId,
            dateModel.getDay(),
            dateModel.getMonth(),
            dateModel.getYear()
        )
    }

    fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return shoppingDao.getProductCategoryList()
        // return MutableLiveData<List<CategoryAndProductModel>>()
    }

    suspend fun getLatestBuyingId(): Long {
        return buyingDao.getLatestBuyingId()
    }

    suspend fun getDateId(customDate: CustomDate): Long {
        var qDate = customDateDao.getDateId(
            customDate.getDay(),
            customDate.getMonth(),
            customDate.getYear()
        )
        if (qDate == null) {
            customDateDao.insert(customDate)//need to take return value from here
            qDate = customDateDao.getDateId(
                customDate.getDay(),
                customDate.getMonth(),
                customDate.getYear()
            )
        }
        return qDate.getDateId()
    }

    suspend fun insertBuyingItem(buyItem: BuyItem): Long {
        return buyingDao.insert(buyItem)
    }

    suspend fun getPurchaseHistoryFromBuyingId(buyingId: Long): List<PurchaseHistory> {
        return purchaseShoppingCategoryDao.getPurchaseHistoryByBuyingId(buyingId)
    }

    suspend fun getBuingHistory(): LiveData<List<BuyingHistory>> {
        return buyingDao.getBuyingHistory()
    }

    suspend fun updatePurchaseItem(purchaseItem: PurchaseItem) {
        purchaseDao.update(purchaseItem)
    }

    suspend fun updateDateId(dateId: Long, buyingId: Long) {
        buyingDao.updateDateId(dateId, buyingId);
    }
}