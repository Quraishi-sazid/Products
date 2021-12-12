package com.example.hishab.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hishab.db.dao.*
import com.example.hishab.models.entities.*
import javax.inject.Inject


@Database(entities = arrayOf(Category::class,Product::class,PurchaseItem::class,Shopping::class,CustomDate::class), version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ShoppingDao(): ProductDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun PurchaseDao(): PurchaseDao
    abstract fun BuyingDao(): BuyingDao
    abstract fun PurchaseShoppingCategoryDao(): PurchaseHistoryDao
    abstract fun customDao(): DateDao

    @Inject
    lateinit var INSTANCE: AppDatabase

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hishab_database"
                ).
                    allowMainThreadQueries().
                fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

}