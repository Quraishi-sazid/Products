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
    suspend fun insertAll(vararg  shoppingItems: ShoppingItem)
    //@Transaction
    /*@Query("select * from category")
    suspend fun getShoppingTable():List<CategoryAndShoppingItem>
    @Query("select * from product_table where productName=:itemName and category_id=:categoryId")
    suspend fun getShoppingItemFromItemNameAndCategoryId(itemName:String,categoryId:Int):ShoppingItem
    @Query("select s.productName as productName,s._productId as productId,c.category_id as categoryId,c.category_name as categoryName from product_table s inner join category c where s.category_id==c.category_id")
    fun getProductCategoryList():LiveData<List<CategoryAndProductModel>>*/
}