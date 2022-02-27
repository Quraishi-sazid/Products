package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.entities.Product

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product):Long
    @Query("select * from product_table where product_name=:itemName and category_id=:categoryId")
    suspend fun getShoppingItemFromItemNameAndCategoryId(itemName:String,categoryId:Long):Product
    @Query("select s.product_name as productName,s.product_id as productId,c.category_id as categoryId,c.category_name as categoryName from  category c left join product_table s on c.category_id=s.category_id")
    fun getProductCategoryList():LiveData<List<CategoryAndProductModel>>
}