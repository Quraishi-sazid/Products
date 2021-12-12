package com.example.hishab.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tbl_shopping")
data class Shopping(
    private @ColumnInfo(name = "date_id") var dateId: Long = 0,
    private @ColumnInfo(name = "time") var time: Long = 0,
    private @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "shopping_id") var shoppingId: Long = 0
) {
    fun getShoppingId(): Long {
        return shoppingId;
    }

    fun getDateId(): Long {
        return dateId;
    }

    fun getTime(): Long {
        return time;
    }
}