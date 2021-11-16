package com.example.hishab.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "buying_entity")
class BuyItem {
    private @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "buying_id") var buyingId:Int=0
    private @ColumnInfo(name = "date_id")  var dateId:Int = 0
    private @ColumnInfo(name = "time")  var time:Int = 0

    fun getBuyingId():Int
    {
        return  buyingId;
    }
    fun getDateId():Int
    {
        return  dateId;
    }
    fun getTime():Int
    {
        return  time;
    }
}