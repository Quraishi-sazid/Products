package com.example.hishab.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "buying_entity")
data class BuyItem(
        private @ColumnInfo(name = "date_id")  var dateId:Long = 0,
        private @ColumnInfo(name = "time")  var time:Long = 0,
        private @ColumnInfo(name = "item_count")  var ItemCount:Int = 0,
        private @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "buying_id") var buyingId:Long=0
) {
    fun getBuyingId():Long
    {
        return  buyingId;
    }
    fun getDateId():Long
    {
        return  dateId;
    }
    fun getTime():Long
    {
        return  time;
    }
    fun getItemCount():Int
    {
        return ItemCount;
    }
}