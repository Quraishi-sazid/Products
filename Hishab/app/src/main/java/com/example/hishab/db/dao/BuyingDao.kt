package com.example.hishab.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hishab.models.BuyingHistory
import com.example.hishab.models.entities.BuyItem
import com.example.hishab.models.entities.CustomDate

@Dao
interface BuyingDao {
    @Query("select max(buying_id) from buying_entity")
    suspend fun getLatestBuyingId():Long
    @Insert
    suspend fun insert(buyItem: BuyItem):Long
    @Query("select b.buying_id as buyingId,count(b.buying_id) as totalItem,sum(cost) totalCost,day as day,month as month,year as year,time as time from buying_entity b inner join purchase_table p on b.buying_id=p.buying_id inner join custom_date c on b.date_id=c.date_id group by b.buying_id")
    suspend fun getBuyingHistory():List<BuyingHistory>
}