package com.example.hishab.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.hishab.db.dao.CategoryDao
import com.example.hishab.db.dao.PurchaseDao
import com.example.hishab.db.dao.PurchaseShoppingCategoryDao
import com.example.hishab.db.dao.ShoppingDao
import com.example.hishab.di.FooEntryPoint
//import com.example.hishab.di.FooEntryPoint
import com.example.hishab.models.CategoryAndShoppingItem
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.*
import dagger.hilt.EntryPoints


class ShoppingRepository (application: Application) {
    private var categoryDao: CategoryDao
    private var shoppingDao: ShoppingDao
    private var purchaseDao: PurchaseDao
    private var purchaseShoppingCategoryDao: PurchaseShoppingCategoryDao
    var database= EntryPoints.get(application, FooEntryPoint::class.java).database
    //var database= AppDatabase.getDatabase(application)
    init {
        categoryDao=database.CategoryDao()
        shoppingDao=database.ShoppingDao()
        purchaseDao=database.PurchaseDao()
        purchaseShoppingCategoryDao=database.PurchaseShoppingCategoryDao()
    }
    suspend fun insertCategory(category: Category)
    {
        categoryDao.insertAll(category)
    }

    suspend fun getCategoryIdFromName(name:String):Category
    {
        return categoryDao.getCategoryIdFromName(name);
    }
    suspend fun insertShopping(shoppingItem:ShoppingItem)
    {
        shoppingDao.insertAll(shoppingItem)
    }
    suspend fun getShoppingList():List<CategoryAndShoppingItem>
    {
        return shoppingDao.getShoppingTable();
    }
    suspend fun getShoppingItemFromNameAndCId(itemName:String,categoryId:Int):ShoppingItem
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
        return purchaseShoppingCategoryDao.getPurchaseHistory()
    }
    suspend fun getTotalCostByCategoryFromDate():List<CategoryCostModel>//live data needed
    {
        return purchaseDao.getTotalCostByCategoryFromDate(0,0,0)
    }

    suspend fun deletePurchaseHistory(position: Int) {
         purchaseDao.deleteByPurchaseId(position)
    }

    suspend fun updatePurchaseItem(
        shoppingItem: ShoppingItem,
        purchaseItem: PurchaseItem
    ) {
        purchaseDao.update(shoppingItem.itemId,purchaseItem.getPurchaseId(),purchaseItem.getCost(),purchaseItem.day,purchaseItem.month,purchaseItem.year);
    }
}