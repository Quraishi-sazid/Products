package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.hishab.models.CategoryProxy
import com.example.hishab.models.entities.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category):Long
    @Query("SELECT * FROM category")
    suspend fun getAll():  List<Category>
    @Query("SELECT * FROM category where category_name like :name")
    suspend fun getCategoryIdFromName(name:String): Category
    @Query("Select * FROM category where category_id=:id")
    suspend fun getCategoryById(id:Long):Category
    @Query("Select -1 as proxyId, c.category_id as categoryId, c.category_name as categoryName,count(p.product_id) as totalProductMappedWithThis from category as c left join product_table as p on c.category_id=p.category_id group by c.category_id")
    fun getCategoryWithTotalProductMapped():LiveData<List<CategoryProxy>>
    @Insert
    suspend fun insertCategories(vararg category: Category)
    @Query("Delete from category where category_id=:deleteId")
    suspend fun deleteCategoryById(deleteId: Long)
    @Update
    suspend fun updateCategory(updateCategory: Category)
}