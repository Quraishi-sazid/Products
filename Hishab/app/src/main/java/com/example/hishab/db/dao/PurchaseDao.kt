package com.example.hishab.db.dao

import androidx.room.*
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.PurchaseItem

@Dao
interface PurchaseDao {
    @Insert
    suspend fun insertAll(vararg purchaseItems: PurchaseItem)

    @Transaction
    @Query("select * from purchase_table")
    suspend fun getAllPurchase(): List<PurchaseItem>
    @Query("select c.category_id as CategoryId,c.category_name as CategoryName,sum(p.cost) as Cost from purchase_table p inner join product_table s on p.purchase_id=s.product_id inner join category c on s.category_id=c.category_id inner join buying_entity b on p.buying_id=b.buying_id inner join custom_date d on b.date_id=d.date_id where d.day>=:day and d.month>=:month and d.year>=:year group by c.category_id")
    suspend fun getTotalCostByCategoryFromDate(day:Int,month:Int,year:Int):List<CategoryCostModel>
    @Query("delete from purchase_table where purchase_id=:purchaseId")
    suspend fun deleteByPurchaseId(purchaseId: Long)
    @Query("update purchase_table set product_id=:itemId,cost=:cost where purchase_id=:purchaseId")
    suspend fun updateByPurchaseId(itemId: Int, purchaseId: Int, cost: Int)

    @Update
    suspend fun update(vararg purchaseItems: PurchaseItem)

}