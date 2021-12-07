package com.example.hishab.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hishab.db.dao.*
import com.example.hishab.di.FooEntryPoint
import com.example.hishab.models.BuyingHistory
//import com.example.hishab.di.FooEntryPoint
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.DateModel
import com.example.hishab.models.entities.*
import dagger.hilt.EntryPoints


class Repository (application: Application) {
    private var categoryDao: CategoryDao
    private var shoppingDao: ShoppingDao
    private var purchaseDao: PurchaseDao
    private var purchaseShoppingCategoryDao: PurchaseShoppingCategoryDao
    private var buyingDao: BuyingDao
    private var customDateDao: DateDao
    var database= EntryPoints.get(application, FooEntryPoint::class.java).database
    init {
        categoryDao=database.CategoryDao()
        shoppingDao=database.ShoppingDao()
        purchaseDao=database.PurchaseDao()
        purchaseShoppingCategoryDao=database.PurchaseShoppingCategoryDao()
        buyingDao=database.BuyingDao()
        customDateDao=database.customDao()
    }
    suspend fun insertCategory(category: Category):Long
    {
        return categoryDao.insertAll(category)
    }

    suspend fun getCategoryIdFromName(name:String):Category
    {
        return categoryDao.getCategoryIdFromName(name);
    }
    suspend fun insertShopping(shoppingItem:ShoppingItem):Long
    {
        return shoppingDao.insert(shoppingItem)
    }
    suspend fun getShoppingItemFromNameAndCId(itemName:String,categoryId:Long):ShoppingItem
    {
        return shoppingDao.getShoppingItemFromItemNameAndCategoryId(itemName,categoryId);
    }
    suspend fun insertPurchaseItem(purchaseItem:PurchaseItem)
    {
        return purchaseDao.insertAll(purchaseItem)
    }
    suspend fun getPurchaseItems():List<PurchaseItem>
    {
        return purchaseDao.getAllPurchase()
    }
    suspend fun getPurchaseHistory(): LiveData<List<PurchaseHistory>>//live data needed
    {
        //return purchaseShoppingCategoryDao.getPurchaseHistory()
        return MutableLiveData<List<PurchaseHistory>>();
    }
    suspend fun getTotalCostByCategoryFromDate():List<CategoryCostModel>//live data needed
    {
        //return purchaseDao.getTotalCostByCategoryFromDate(0,0,0)
        return ArrayList<CategoryCostModel>()
    }

    suspend fun deletePurchaseHistory(position: Long) {
         purchaseDao.deleteByPurchaseId(position)
    }

    suspend fun updatePurchaseItem(
        shoppingItem: ShoppingItem,
        purchaseItem: PurchaseItem
    ) {
        //purchaseDao.update(shoppingItem.productId,purchaseItem.getPurchaseId(),purchaseItem.getCost(),purchaseItem.day,purchaseItem.month,purchaseItem.year);
    }

    suspend fun getdetailsOfCategoryfromDate(categoryId: Int, dateModel: DateModel):LiveData<List<PurchaseHistory>> {
       //return purchaseShoppingCategoryDao.getdetailsOfCategoryfromDate(categoryId,dateModel.day,dateModel.month,dateModel.year)
        return MutableLiveData<List<PurchaseHistory>>();
    }
    fun getProductCategoryList():LiveData<List<CategoryAndProductModel>>
    {
        return shoppingDao.getProductCategoryList()
       // return MutableLiveData<List<CategoryAndProductModel>>()
    }

    suspend fun getLatestBuyingId():Long {
       return buyingDao.getLatestBuyingId()
    }
    suspend fun getDateId(customDate:CustomDate):Long
    {
        var qDate=customDateDao.getDateId(customDate.getDay(),customDate.getMonth(),customDate.getYear())
        if(qDate==null)
        {
            customDateDao.insert(customDate)//need to take return value from here
            qDate=customDateDao.getDateId(customDate.getDay(),customDate.getMonth(),customDate.getYear())
        }
        return qDate.getDateId()
    }
    suspend fun insertBuyingItem(buyItem:BuyItem):Long
    {
      var a= buyingDao.insert(buyItem)
        return  a
    }
    suspend fun getPurchaseHistoryFromBuyingId(buyingId:Long):List<PurchaseHistory>
    {
        return purchaseShoppingCategoryDao.getPurchaseHistoryByBuingId(buyingId)
    }

    suspend fun getBuingHistory():List<BuyingHistory>
    {
     //   return ArrayList<BuyingHistory>()
        return buyingDao.getBuyingHistory()
    }
}