package com.example.hishab.db.dao

import androidx.room.*
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.entities.PurchaseItem

@Dao
interface PurchaseDao {
    @Insert
    suspend fun insertAll(vararg purchaseItems: PurchaseItem)

    @Transaction
    @Query("select * from tbl_shopping_item")
    suspend fun getAllPurchase(): List<PurchaseItem>

    @Query("select c.category_id as CategoryId,c.category_name as CategoryName,sum(p.cost) as Cost from tbl_shopping_item p inner join product_table s on p.purchase_id=s.product_id inner join category c on s.category_id=c.category_id inner join tbl_shopping b on p.shopping_id=b.shopping_id inner join custom_date d on b.date_id=d.date_id  group by c.category_id")
    suspend fun getAllTimeTotalCostByCategory(): List<CategoryCostModel>

    @Query("select c.category_id as CategoryId,c.category_name as CategoryName,sum(p.cost) as Cost from tbl_shopping_item p inner join product_table s on p.purchase_id=s.product_id inner join category c on s.category_id=c.category_id inner join tbl_shopping b on p.shopping_id=b.shopping_id inner join custom_date d on b.date_id=d.date_id where d.year=:year and d.month=:month  group by c.category_id")
    suspend fun getCategoryCostFromMonthAndYear(month:Int,year:Int): List<CategoryCostModel>

    @Query("delete from tbl_shopping_item where purchase_id=:purchaseId")
    suspend fun deleteByPurchaseId(purchaseId: Long)
    @Query("update tbl_shopping_item set product_id=:itemId,cost=:cost where purchase_id=:purchaseId")
    suspend fun updateByPurchaseId(itemId: Int, purchaseId: Int, cost: Int)

    @Update
    suspend fun update(vararg purchaseItems: PurchaseItem)

}