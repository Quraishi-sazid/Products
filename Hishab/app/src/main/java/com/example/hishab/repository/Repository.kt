package com.example.hishab.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.*
import com.example.hishab.models.*
import com.example.hishab.models.entities.*
import io.reactivex.Flowable


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

    fun getBuingHistory(): LiveData<List<ShoppingHistory>> {
        return shoppingDao.getBuyingHistory()
    }

    suspend fun updatePurchaseItem(purchaseItem: PurchaseItem) {
        purchaseDao.update(purchaseItem)
    }

    suspend fun updateDateId(dateId: Long, buyingId: Long) {
        shoppingDao.updateDateId(dateId, buyingId);
    }

    fun getCategoryWithProductTableMap(): LiveData<List<CategoryProxy>> {
        return categoryDao.getCategoryWithTotalProductMapped();
    }

    suspend fun deleteCategoryById(deleteId: Long) {
        categoryDao.deleteCategoryById(deleteId)
        budgetDao.deleteByCategoryId(deleteId)
    }

    suspend fun updateCategory(updateCategory: Category) {
        categoryDao.updateCategory(updateCategory)
    }

    fun getPurchaseCountOfProductId(productId: Long): Int {
        return productDao.getPurchaseCountOfProductId(productId)
    }

    fun deleteByProductById(productId: Long) {
        productDao.deleteByProductById(productId)
    }

    fun getBudgetList(month: Int, year: Int): Flowable<List<Budget>> {
        return budgetDao.getBudgetFlowable(month, year)
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

    suspend fun updateTimeForShopping(milisec: Long,shoppingId:Long) {
        shoppingDao.updateShoppingTime(milisec,shoppingId)
    }

    fun getCategoryBudgetList(month:Int,year:Int): List<BudgetCategoryQuery> {
        var list = budgetDao.getCategoryAndBudgetList(year,month)
        return list
    }

    suspend fun updateBudgetById(categoryId: Long, budget: Int, month: Int, year: Int){
         budgetDao.updateBudgetById(categoryId,budget,month,year)
    }

    fun insertBudget(budget: Budget) {
        budgetDao.insert(budget)
    }
    fun getAllCategories():List<Category>{
        return categoryDao.getAllCategory()
    }

    suspend fun getAllCategoriesSuspended():List<Category>{
        return categoryDao.getAllCategory()
    }

    fun getProductDetailsLiveData(id:Long):LiveData<List<ProductDetailsModel>>{
        return productDao.getProductDetails(id)
    }


}