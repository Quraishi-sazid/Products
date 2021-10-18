package com.example.hishab.db.dao

import androidx.room.*
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.PurchaseItem
@Dao
interface PurchaseDao {
    @Insert
    suspend fun insertAll(vararg  purchaseItems: PurchaseItem)
    @Transaction
    @Query("select * from purchase_table")
    suspend fun getAllPurchase():List<PurchaseItem>
   // @Query("select c.category_id as CategoryId,c.category_name as CategoryName,sum(p.cost) as Cost from purchase_table p inner join shopping_table s on p.shopping_id=s.shopping_id inner join category c on s.category_id=c.category_id where p.day>=:day and p.month>=:month and p.year>=:year group by c.category_id")
    @Query("select c.category_id as CategoryId,c.category_name as CategoryName,sum(p.cost) as Cost " +
            "from purchase_table p inner join shopping_table s on p.shopping_id=s.shopping_id " +
            "inner join category c on s.category_id=c.category_id where p.day>=:day and p.month>=:month and p.year>=:year " +
            "group by c.category_id")
    suspend fun getTotalCostByCategoryFromDate(day:Int,month:Int,year:Int):List<CategoryCostModel>
}