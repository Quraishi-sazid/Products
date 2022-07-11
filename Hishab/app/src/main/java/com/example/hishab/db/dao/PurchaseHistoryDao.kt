package com.example.hishab.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.hishab.models.PurchaseHistory
import io.reactivex.Observable


@Dao
interface PurchaseHistoryDao {
    //to add date limitation add this clause after category.category_id=:categoryId "and (year>:year or year=:year and month>:month or year=:year and month=:month and day>=:day )"
    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year,category.category_id as categoryId,product_table.product_id as productId,time as time,tbl_shopping_item.shopping_id as buyingId from category inner join product_table on category.category_id=product_table.category_id inner join tbl_shopping_item on product_table.product_id=tbl_shopping_item.product_id inner join tbl_shopping on tbl_shopping_item.shopping_id=tbl_shopping.shopping_id inner join custom_date on tbl_shopping.date_id=custom_date.date_id where category.category_id=:categoryId and time <:time order by time desc limit :loadSize")
    fun getCategoryDetails(
        categoryId: Int,
        time: Long,
        loadSize: Int
    ): List<PurchaseHistory>

    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year,category.category_id as categoryId,product_table.product_id as productId,time as time,tbl_shopping_item.shopping_id as buyingId from category inner join product_table on category.category_id=product_table.category_id inner join tbl_shopping_item on product_table.product_id=tbl_shopping_item.product_id inner join tbl_shopping on tbl_shopping_item.shopping_id=tbl_shopping.shopping_id inner join custom_date cd on tbl_shopping.date_id=cd.date_id where time<:time and category.category_id=:categoryId and cd.month=:month and cd.year=:year order by time desc limit :loadSize")
    fun getCategoryDetailsOfMonth(
        time: Long,
        loadSize: Int,
        categoryId: Int,
        month: Int,
        year: Int
    ): List<PurchaseHistory>

    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year,category.category_id as categoryId,product_table.product_id as productId,time as time,tbl_shopping_item.shopping_id as buyingId from category inner join product_table on category.category_id=product_table.category_id inner join tbl_shopping_item on product_table.product_id=tbl_shopping_item.product_id inner join tbl_shopping on tbl_shopping_item.shopping_id=tbl_shopping.shopping_id inner join custom_date on tbl_shopping.date_id=custom_date.date_id where time <:time order by time desc limit :loadSize")
    fun getPurchaseHistory(time: Long, loadSize: Int): List<PurchaseHistory>

    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,-1 as day,-1 as month,-1 as year,category.category_id as categoryId,product_table.product_id as productId,tbl_shopping_item.shopping_id as buyingId from category inner join product_table on category.category_id=product_table.category_id inner join tbl_shopping_item on product_table.product_id=tbl_shopping_item.product_id where tbl_shopping_item.shopping_id=:shoppingId order by purchase_id desc")
    fun getPurchaseHistoryByBuyingId(shoppingId: Long): List<PurchaseHistory>

}