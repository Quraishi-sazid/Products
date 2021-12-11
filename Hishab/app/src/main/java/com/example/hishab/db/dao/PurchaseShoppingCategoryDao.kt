package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.SkipQueryVerification
import com.example.hishab.models.entities.PurchaseHistory


@Dao
interface PurchaseShoppingCategoryDao {
    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year,category.category_id as categoryId,product_table.product_id as shoppingId from category inner join product_table on category.category_id=product_table.category_id inner join purchase_table on product_table.product_id=purchase_table.product_id inner join buying_entity on purchase_table.buying_id=buying_entity.buying_id inner join custom_date on buying_entity.date_id=custom_date.date_id where category.category_id=:categoryId and day=:day and month=:month and year=:year order by purchase_id desc")
    fun getDetailsOfCategoryFromDate(
        categoryId: Int,
        day: Int,
        month: Int,
        year: Int
    ): LiveData<List<PurchaseHistory>>


    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year,category.category_id as categoryId,product_table.product_id as shoppingId from category inner join product_table on category.category_id=product_table.category_id inner join purchase_table on product_table.product_id=purchase_table.product_id inner join buying_entity on purchase_table.buying_id=buying_entity.buying_id inner join custom_date on buying_entity.date_id=custom_date.date_id order by purchase_id desc")
    fun getPurchaseHistory(): LiveData<List<PurchaseHistory>>

    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,-1 as day,-1 as month,-1 as year,category.category_id as categoryId,product_table.product_id as productId from category inner join product_table on category.category_id=product_table.category_id inner join purchase_table on product_table.product_id=purchase_table.product_id where buying_id=:buyingId order by purchase_id desc")
    fun getPurchaseHistoryByBuyingId(buyingId: Long): List<PurchaseHistory>

}