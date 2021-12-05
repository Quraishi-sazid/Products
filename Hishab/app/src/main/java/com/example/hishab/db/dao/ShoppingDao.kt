package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.hishab.models.CategoryAndShoppingItem
import com.example.hishab.models.entities.CategoryAndProductModel
import com.example.hishab.models.entities.ShoppingItem

@Dao
interface ShoppingDao {
    @Insert
    suspend fun insert(shoppingItem: ShoppingItem):Long
    //@Transaction
    /*@Query("select * from category")
    suspend fun getShoppingTable():List<CategoryAndShoppingItem>*/
    @Query("select * from product_table where product_name=:itemName and category_id=:categoryId")
    suspend fun getShoppingItemFromItemNameAndCategoryId(itemName:String,categoryId:Long):ShoppingItem
    @Query("select s.product_name as productName,s.product_id as productId,c.category_id as categoryId,c.category_name as categoryName from product_table s inner join category c where s.category_id==c.category_id")
    fun getProductCategoryList():LiveData<List<CategoryAndProductModel>>
}