package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hishab.models.ShoppingHistory
import com.example.hishab.models.entities.Shopping

@Dao
interface ShoppingDao {
    @Query("select max(shopping_id) from tbl_shopping")
    suspend fun getLatestBuyingId():Long
    @Insert
    suspend fun insert(shopping: Shopping):Long
    @Query("select b.shopping_id as buyingId,count(b.shopping_id) as totalItem,sum(cost) totalCost,day as day,month as month,year as year,time as time from tbl_shopping b inner join tbl_shopping_item p on b.shopping_id=p.shopping_id inner join custom_date c on b.date_id=c.date_id group by b.shopping_id order by b.time desc")
    fun getBuyingHistory():LiveData<List<ShoppingHistory>>
    @Query("update tbl_shopping set date_id=:dateId where shopping_id=:buyingId")
    suspend fun updateDateId(dateId:Long,buyingId:Long)
    @Insert
    suspend fun insertShoppingList(vararg shoppingList: Shopping)
    @Query("update tbl_shopping set time=:time where shopping_id=:shoppingId")
    suspend fun updateShoppingTime(time:Long,shoppingId:Long)
}