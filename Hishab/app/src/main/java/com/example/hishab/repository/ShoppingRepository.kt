package com.example.hishab.repository

import android.app.Application
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.CategoryDao
import com.example.hishab.db.dao.PurchaseDao
import com.example.hishab.db.dao.PurchaseShoppingCategoryDao
import com.example.hishab.db.dao.ShoppingDao
import com.example.hishab.models.CategoryAndShoppingItem
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.*

class ShoppingRepository(application: Application) {
    private var categoryDao: CategoryDao
    private var shoppingDao: ShoppingDao
    private var purchaseDao: PurchaseDao
    private var purchaseShoppingCategoryDao: PurchaseShoppingCategoryDao
    private val database = AppDatabase.getDatabase(application)

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
    suspend fun getAll():List<Category>
    {
        return categoryDao.getAll();
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
    suspend fun getPurchaseHistory():List<PurchaseHistory>
    {
        return purchaseShoppingCategoryDao.getPurchaseHistory()
    }
    suspend fun getTotalCostByCategoryFromDate():List<CategoryCostModel>
    {
        return purchaseDao.getTotalCostByCategoryFromDate(0,0,0)
    }



}