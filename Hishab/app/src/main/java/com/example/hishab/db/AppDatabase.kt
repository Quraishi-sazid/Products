package com.example.hishab.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hishab.db.dao.CategoryDao
import com.example.hishab.db.dao.PurchaseDao
import com.example.hishab.db.dao.ShoppingDao
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.db.dao.PurchaseShoppingCategoryDao
import com.example.hishab.models.entities.ShoppingItem
import com.example.hishab.utils.Converters


@Database(entities = arrayOf(Category::class,ShoppingItem::class,PurchaseItem::class), version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ShoppingDao(): ShoppingDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun PurchaseDao(): PurchaseDao
    abstract fun PurchaseShoppingCategoryDao(): PurchaseShoppingCategoryDao


    private var INSTANCE: AppDatabase? = null

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
                ).fallbackToDestructiveMigration().
                build()
                INSTANCE = instance
                instance
            }
        }

    }

}