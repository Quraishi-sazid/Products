package com.example.hishab.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.SkipQueryVerification
import com.example.hishab.models.entities.PurchaseHistory


@Dao
interface PurchaseShoppingCategoryDao {
    @Query("select category_name as categoryName,item_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year from category inner join shopping_table on category.category_id=shopping_table.category_id inner join purchase_table on shopping_table.shopping_id=purchase_table.shopping_id order by purchase_id desc")
    fun getPurchaseHistory():List<PurchaseHistory>
    @Query("select category_name as categoryName,item_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year from category inner join shopping_table on category.category_id=shopping_table.category_id inner join purchase_table on shopping_table.shopping_id=purchase_table.shopping_id where category_name=:pcategoryName order by purchase_id desc")
    fun getPurchaseHistoryByCategoryName(pcategoryName:String):List<PurchaseHistory>
    @Query("select category_name as categoryName,item_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year from category inner join shopping_table on category.category_id=shopping_table.category_id inner join purchase_table on shopping_table.shopping_id=purchase_table.shopping_id where item_name=:itemName order by purchase_id desc")
    fun getPurchaseHistoryByItemName(itemName:String):List<PurchaseHistory>
}