package com.example.hishab.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hishab.db.dao.*
import com.example.hishab.models.entities.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@Database(
    entities = arrayOf(
        Category::class,
        Product::class,
        PurchaseItem::class,
        Shopping::class,
        CustomDate::class
    ), version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ShoppingDao(): ProductDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun PurchaseDao(): PurchaseDao
    abstract fun BuyingDao(): ShoppingDao
    abstract fun PurchaseShoppingCategoryDao(): PurchaseHistoryDao
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
                            INSTANCE?.CategoryDao()
                                ?.insertCategories(*getDefaultCategoryList().map { it }
                                    .toTypedArray())
                        }
                    }
                }).allowMainThreadQueries().fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun getDefaultCategoryList(): List<Category> {
            var categoryList = ArrayList<Category>()
            categoryList.add(Category("Grocery"))
            categoryList.add(Category("Transport"))
            categoryList.add(Category("Medicine"))
            categoryList.add(Category("Home Rent"))
            return categoryList
        }

    }

}