package com.example.hishab.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hishab.models.entities.BuyItem
import com.example.hishab.models.entities.CustomDate

@Dao
interface BuyingDao {
    @Query("select max(buying_id) from buying_entity")
    suspend fun getLatestBuyingId():Long
    @Insert
    suspend fun insert(buyItem: BuyItem):Long
}