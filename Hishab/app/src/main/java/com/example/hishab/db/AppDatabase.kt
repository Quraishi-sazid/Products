package com.example.hishab.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hishab.db.dao.*
import com.example.hishab.models.entities.*
import com.example.hishab.models.entities.views.Vw_category_spent
import com.example.hishab.utils.Util
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//https://github.com/rafi0101/Android-Room-Database-Backup
@Database(
    entities = arrayOf(
        Category::class,
        Product::class,
        PurchaseItem::class,
        Shopping::class,
        CustomDate::class,
        Budget::class
    ),views =[Vw_category_spent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ProductDao(): ProductDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun PurchaseDao(): PurchaseDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun PurchaseShoppingCategoryDao(): PurchaseHistoryDao
    abstract fun BudgetDao(): BudgetDao
    abstract fun customDao(): DateDao


    companion object {
        @Volatile
         var INSTANCE: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hishab_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch {
                            insertPredefinedData()
                        }
                    }
                }).allowMainThreadQueries().fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun insertPredefinedData() {
            INSTANCE?.CategoryDao()
                ?.insertCategories(*getDefaultCategoryList().map { it }
                    .toTypedArray())
            INSTANCE?.ProductDao()
                ?.insertProducts(*getDefaultProductList().map { it }
                    .toTypedArray())
            INSTANCE?.customDao()
                ?.insertCustomDate(*getDefaultCustomDate().map { it }
                    .toTypedArray())
            INSTANCE?.shoppingDao()
                ?.insertShoppingList(*getShoppingList().map { it }
                    .toTypedArray())
            INSTANCE?.PurchaseDao()
                ?.insertAll(*getPurchaseList().map { it }
                    .toTypedArray())
            INSTANCE?.BudgetDao()
                ?.insertAll(*getBudgetList().map { it }
                    .toTypedArray())
        }

        private fun getBudgetList(): List<Budget> {
            var budgetList=ArrayList<Budget>()
            budgetList.add(Budget(1,1000,3,2022))
            budgetList.add(Budget(2,2000,3,2022))
            budgetList.add(Budget(3,3000,3,2022))
            budgetList.add(Budget(4,25000,3,2022))
            return budgetList
        }

        private fun getPurchaseList(): List<PurchaseItem> {
            var purchaseList=ArrayList<PurchaseItem>()
            purchaseList.add(PurchaseItem(1,1,12))
            purchaseList.add(PurchaseItem(1,2,20))

            purchaseList.add(PurchaseItem(2,3,200))
            purchaseList.add(PurchaseItem(2,4,10))

            purchaseList.add(PurchaseItem(3,5,100))
            purchaseList.add(PurchaseItem(3,6,50))

            purchaseList.add(PurchaseItem(4,7,18000))
            purchaseList.add(PurchaseItem(4,8,975))
            return purchaseList
        }

        private fun getShoppingList(): List<Shopping> {
            var shoppingList=ArrayList<Shopping>()
            shoppingList.add(Shopping(1,Util.getCurrentMili()))
            shoppingList.add(Shopping(1,Util.getCurrentMili()))
            shoppingList.add(Shopping(1,Util.getCurrentMili()))
            shoppingList.add(Shopping(2,Util.getCurrentMili()))
            return shoppingList
        }

        private fun getDefaultCustomDate(): List<CustomDate> {
            var customDateList=ArrayList<CustomDate>()
            customDateList.add(CustomDate(2022,3,1))
            customDateList.add(CustomDate(2022,3,2))
            customDateList.add(CustomDate(2022,2,2))
            customDateList.add(CustomDate(2022,2,4))
            return customDateList
        }

        private fun getDefaultProductList(): List<Product> {
            var productList=ArrayList<Product>()
            productList.add(Product("rice",1L));
            productList.add(Product("dal",1L))
            productList.add(Product("Uber rent",2L))
            productList.add(Product("RickshawRent",2L))
            productList.add(Product("Seclo",3L))
            productList.add(Product("Napa",3L))
            productList.add(Product("House Rent",4L))
            productList.add(Product("Gas Bill",4L))

            return productList
        }

        private fun getDefaultCategoryList(): List<Category> {
            var categoryList = ArrayList<Category>()
            categoryList.add(Category("Grocery"))
            categoryList.add(Category("Transport"))
            categoryList.add(Category("Medicine"))
            categoryList.add(Category("Utility"))
            return categoryList
        }

    }

}