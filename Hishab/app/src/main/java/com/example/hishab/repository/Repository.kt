package com.example.hishab.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.*
import com.example.hishab.models.*
import com.example.hishab.models.entities.*
import io.reactivex.Flowable
import io.reactivex.Observable


class Repository(application: Application) {
    private var categoryDao: CategoryDao
    private var productDao: ProductDao
    private var purchaseDao: PurchaseDao
    private var purchaseHistoryDao: PurchaseHistoryDao
    private var shoppingDao: ShoppingDao
    private var customDateDao: DateDao
    private var budgetDao: BudgetDao
    var database = AppDatabase.getDatabase(application)
    //var database = EntryPoints.get(application, FooEntryPoint::class.java).database

    init {
        categoryDao = database.CategoryDao()
        productDao = database.ProductDao()
        purchaseDao = database.PurchaseDao()
        purchaseHistoryDao = database.PurchaseShoppingCategoryDao()
        shoppingDao = database.shoppingDao()
        customDateDao = database.customDao()
        budgetDao = database.BudgetDao()
    }

    suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category)
    }

    suspend fun getCategoryIdFromName(name: String): Category {
        return categoryDao.getCategoryFromName(name);
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

    fun getProductCategoryListLeftJoin(): LiveData<List<CategoryAndProductModel>> {
        return productDao.getProductCategoryListLeftJoin()
    }

    fun getProductCategoryListInnerJoin(): LiveData<List<CategoryAndProductModel>> {
        return productDao.getProductCategoryListInnerJoin()
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

    suspend fun getCategoryWithProductTableMap(): LiveData<List<CategoryProxy>> {
        return categoryDao.getCategoryWithTotalProductMapped();
    }

    suspend fun deleteCategoryById(deleteId: Long) {
        categoryDao.deleteCategoryById(deleteId)
        budgetDao.deleteByCategoryId(deleteId)
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
            var quariedCategory = categoryDao.getCategoryFromName(categoryName)
            var insertedCategoryId = -1L
            if (quariedCategory == null) {
                insertedCategoryId = insertCategory(Category(categoryName))
            } else
                insertedCategoryId = quariedCategory.categoryId
            return insertShopping(Product(productName, insertedCategoryId))
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

    fun updateProductName(productId: Long, productName: String) {
        productDao.updateProductName(productName, productId)
    }

    fun getPurchaseCountOfProductId(productId: Long): Int {
        return productDao.getPurchaseCountOfProductId(productId)
    }

    fun deleteByProductById(productId: Long) {
        productDao.deleteByProductById(productId)
    }

    fun getBudgetList(month: Int, year: Int): Flowable<List<Budget>> {
        return budgetDao.getBudgetFlowable(month, year)
/*       return Observable.create<Budget>{
            it.onNext(Budget())
        }.toFlowable(BackpressureStrategy.DROP)*/
    }

    fun getCategoryNameFromCategoryId(categoryId: Long): String? {
        return categoryDao.getCategoryNameFromCategoryId(categoryId)
    }

    fun getCategorySpentsOfMonth(
        categoryIdList: List<Long>,
        month: Int,
        year: Int
    ): List<CategoryCostModel> {
        return budgetDao.getCategorySpents(categoryIdList, month, year)
    }

    fun updateBudgetList(budgetList: List<Budget>) {
        budgetDao.updateBudgetList(budgetList)
    }

    fun getPreviousMonthsBudgetAndSpentHistory(
        year: Int,
        month: Int
    ): Flowable<List<MonthlySpentModel>> {
        return budgetDao.getPreviousBudgetSpentList(month, year)
    }

    fun getBudgetFromMonthAndYear(year: Int, month: Int): Int {
        return budgetDao.getBudgetFromMonthAndYear(year, month)
    }

    suspend fun getCategoryCostFromMonthAndYear(month: Int, year: Int): List<CategoryCostModel> {
        return purchaseDao.getCategoryCostFromMonthAndYear(month, year)
    }

    suspend fun getProductCountMappedWithCategoryId(id: Long): Int {
        return productDao.getProductCountMappedWithCategoryId(id)
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