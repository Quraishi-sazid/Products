package com.example.hishab.models.entities

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "custom_date")
class CustomDate() {
    private @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "date_id") var _dateId:Int=0
    private @ColumnInfo(name = "day")  var day:Int = 0
    private @ColumnInfo(name = "month")  var month:Int = 0
    private @ColumnInfo(name = "year")  var year:Int = 0
    fun getDay():Int
    {
        return day
    }
    fun getMonth():Int
    {
        return month
    }
    fun getYear():Int
    {
        return year
    }
}