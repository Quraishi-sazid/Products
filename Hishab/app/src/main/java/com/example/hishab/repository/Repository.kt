package com.example.hishab.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.*
import com.example.hishab.models.*
import com.example.hishab.models.entities.*


class Repository(application: Application) {
    private var categoryDao: CategoryDao
    private var productDao: ProductDao
    private var purchaseDao: PurchaseDao
    private var purchaseHistoryDao: PurchaseHistoryDao
    private var shoppingDao: ShoppingDao
    private var customDateDao: DateDao
    var database = AppDatabase.getDatabase(application)
    //var database = EntryPoints.get(application, FooEntryPoint::class.java).database

    init {
        categoryDao = database.CategoryDao()
        productDao = database.ShoppingDao()
        purchaseDao = database.PurchaseDao()
        purchaseHistoryDao = database.PurchaseShoppingCategoryDao()
        shoppingDao = database.BuyingDao()
        customDateDao = database.customDao()
    }

    suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category)
    }

    suspend fun getCategoryIdFromName(name: String): Category {
        return categoryDao.getCategoryIdFromName(name);
    }

    suspend fun insertShopping(product: Product): Long {
        return productDao.insert(product)
    }

    suspend fun getShoppingItemFromNameAndCId(itemName: String, categoryId: Long): Product {
        return productDao.getShoppingItemFromItemNameAndCategoryId(itemName, categoryId);
    }

    suspend fun insertPurchaseItem(purchaseItem: PurchaseItem) {
        return purchaseDao.insertAll(purchaseItem)
    }

    suspend fun getPurchaseHistory(): LiveData<List<PurchaseHistory>>//live data needed
    {
        return purchaseHistoryDao.getPurchaseHistory()
    }

    suspend fun getTotalCostByCategoryFromDate(): List<CategoryCostModel>//live data needed
    {
        return purchaseDao.getTotalCostByCategoryFromDate(0, 0, 0)
    }

    suspend fun deletePurchaseHistory(position: Long) {
        purchaseDao.deleteByPurchaseId(position)
    }

    suspend fun getDetailsOfCategoryfromDate(
        categoryId: Int,
        dateModel: CustomDate
    ): LiveData<List<PurchaseHistory>> {
        return purchaseHistoryDao.getCategoryDetailsFromDate(
            categoryId,
            dateModel.getDay(),
            dateModel.getMonth(),
            dateModel.getYear()
        )
    }

    fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return productDao.getProductCategoryList()
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

    suspend fun getBuingHistory(): LiveData<List<ShoppingHistory>> {
        return shoppingDao.getBuyingHistory()
    }

    suspend fun updatePurchaseItem(purchaseItem: PurchaseItem) {
        purchaseDao.update(purchaseItem)
    }

    suspend fun updateDateId(dateId: Long, buyingId: Long) {
        shoppingDao.updateDateId(dateId, buyingId);
    }
    suspend fun getCategoryWithProductTableMap():LiveData<List<CategoryProxy>> {
       return categoryDao.getCategoryWithTotalProductMapped();
    }

    suspend fun deleteCategoryById(deleteId: Long) {
        categoryDao.deleteCategoryById(deleteId)
    }

    suspend fun updateCategory(updateCategory: Category) {
        categoryDao.updateCategory(updateCategory)
    }




    suspend fun getProductIdByInsertingInDataBase(
        categoryId: Long,
        categoryName: String,
        productName: String
    ): Long {
        if (categoryId == 0L) {
            var insertedCategoryId=insertCategory(Category(categoryName))
            return insertShopping(Product(0,productName,insertedCategoryId))
        } else {
            var queriedShoppingItem =
                getProductFromCategoryIdAndProductNameByInsertingOrFetching(
                    categoryId,
                    productName
                )
            return queriedShoppingItem.productId
        }
    }

    private suspend fun getProductFromCategoryIdAndProductNameByInsertingOrFetching(
        categoryId: Long,
        productName: String
    ): Product {

        var quariedProduct = getShoppingItemFromNameAndCId(
            productName,
            categoryId
        )
        if (quariedProduct == null) {
            quariedProduct = Product()
            quariedProduct.categoryId = categoryId
            quariedProduct.setProductName(productName)
            quariedProduct.productId = insertShopping(quariedProduct)
        }
        return quariedProduct
    }

/*    public suspend fun getCategoryByInsertingOrFetching(categoryName: String): Category {
        var queriedCategory = getCategoryIdFromName(categoryName)
        if (queriedCategory == null) {
            queriedCategory = Category(categoryName)
            queriedCategory.categoryId = insertCategory(queriedCategory)
        }
        return queriedCategory
    }*/

}