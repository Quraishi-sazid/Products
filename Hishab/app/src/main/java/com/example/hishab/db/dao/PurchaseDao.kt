package com.example.hishab.db.dao

import androidx.room.*
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.ShoppingItem

@Dao
interface PurchaseDao {
    @Insert
    suspend fun insertAll(vararg  purchaseItems: PurchaseItem)
    @Transaction
    @Query("select * from purchase_table")
    suspend fun getAllPurchase():List<PurchaseItem>
    /*@Query("select c.category_id as CategoryId,c.category_name as CategoryName,sum(p.cost) as Cost from purchase_table p inner join product_table s on p.purchase_id=s._productId inner join category c on s.category_id=c.category_id where p.day>=:day and p.month>=:month and p.year>=:year group by c.category_id")
    suspend fun getTotalCostByCategoryFromDate(day:Int,month:Int,year:Int):List<CategoryCostModel>*/
    @Query("delete from purchase_table where purchase_id=:purchaseId")
    suspend fun deleteByPurchaseId(purchaseId: Long)
    @Query("update purchase_table set product_id=:itemId,cost=:cost where purchase_id=:purchaseId")
    suspend fun update(itemId: Int,purchaseId: Int,cost: Int)

}