package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.hishab.models.PurchaseHistory
import io.reactivex.Observable


@Dao
interface PurchaseHistoryDao {
    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year,category.category_id as categoryId,product_table.product_id as shoppingId from category inner join product_table on category.category_id=product_table.category_id inner join tbl_shopping_item on product_table.product_id=tbl_shopping_item.product_id inner join tbl_shopping on tbl_shopping_item.shopping_id=tbl_shopping.shopping_id inner join custom_date on tbl_shopping.date_id=custom_date.date_id where category.category_id=:categoryId /*and day>=:day and month>=:month and year>=:year*/ order by purchase_id desc")
    fun getCategoryDetailsFromDate(
        categoryId: Int/*,
        day: Int,
        month: Int,
        year: Int*/
    ): Observable<List<PurchaseHistory>>

    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,day as day,month as month,year as year,category.category_id as categoryId,product_table.product_id as shoppingId from category inner join product_table on category.category_id=product_table.category_id inner join tbl_shopping_item on product_table.product_id=tbl_shopping_item.product_id inner join tbl_shopping on tbl_shopping_item.shopping_id=tbl_shopping.shopping_id inner join custom_date on tbl_shopping.date_id=custom_date.date_id order by purchase_id desc")
    fun getPurchaseHistory(): Observable<List<PurchaseHistory>>

    @Query("select purchase_id as purchaseId,category_name as categoryName,product_name as ItemName,cost as cost ,description as descrip,-1 as day,-1 as month,-1 as year,category.category_id as categoryId,product_table.product_id as productId from category inner join product_table on category.category_id=product_table.category_id inner join tbl_shopping_item on product_table.product_id=tbl_shopping_item.product_id where tbl_shopping_item.shopping_id=:shoppingId order by purchase_id desc")
    fun getPurchaseHistoryByBuyingId(shoppingId: Long): List<PurchaseHistory>

}