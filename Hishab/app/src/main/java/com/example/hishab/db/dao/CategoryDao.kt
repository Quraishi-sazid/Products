package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hishab.models.entities.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertAll(vararg  categories: Category)
    @Query("SELECT * FROM category")
    suspend fun getAll(): List<Category>
    @Query("SELECT * FROM category where category_name like :name")
    suspend fun getCategoryIdFromName(name:String): Category
    @Query("Select * FROM category where category_id=:id")
    suspend fun getCategoryById(id:Int):Category
}