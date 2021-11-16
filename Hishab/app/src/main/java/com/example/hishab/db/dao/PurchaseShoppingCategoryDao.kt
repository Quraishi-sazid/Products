package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.SkipQueryVerification
import com.example.hishab.models.entities.PurchaseHistory


@Dao
interface PurchaseShoppingCategoryDao {
   /* @Query("select purchase_id as purchaseId,category_name as categoryName,productName as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year,category.category_id as categoryId,product_table._productId as shoppingId from category inner join product_table on category.category_id=product_table.category_id inner join purchase_table on product_table._productId=purchase_table.productId order by purchase_id desc")
    fun getPurchaseHistory(): LiveData<List<PurchaseHistory>>
    @Query("select purchase_id as purchaseId, category_name as categoryName,productName as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year from category inner join product_table on category.category_id=product_table.category_id inner join purchase_table on product_table._productId=purchase_table.productId where category_name=:pcategoryName order by purchase_id desc")
    fun getPurchaseHistoryByCategoryName(pcategoryName:String):List<PurchaseHistory>
    @Query("select purchase_id as purchaseId, category_name as categoryName,productName as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year from category inner join product_table on category.category_id=product_table.category_id inner join purchase_table on product_table._productId=purchase_table.productId where productName=:itemName order by purchase_id desc")
    fun getPurchaseHistoryByItemName(itemName:String):List<PurchaseHistory>
    @Query("select purchase_id as purchaseId, category_name as categoryName,productName as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year from category inner join product_table on category.category_id=product_table.category_id inner join purchase_table on product_table._productId=purchase_table.productId where category.category_id=:categoryId and day>=:day and month>=:month and year>=:year order by purchase_id desc")
    fun getdetailsOfCategoryfromDate(categoryId: Int, day: Int, month: Int, year: Int):LiveData<List<PurchaseHistory>>
*/}