package com.example.hishab.models.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hishab.models.ShoppingHistory


@Entity(tableName = "tbl_shopping")
data class Shopping(
    private @ColumnInfo(name = "date_id") var dateId: Long = 0,
    private @ColumnInfo(name = "time") var time: Long = 0,
    private @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "shopping_id") var shoppingId: Long = 0,
    var remoteId:Long =-1,
    var isSynced: Boolean = false
) {
    constructor(dateId: Long,shoppingHistory: ShoppingHistory?) : this(){
        if(shoppingHistory!=null){
            shoppingId = shoppingHistory.getBuyingId()
            this.dateId = dateId
            time = shoppingHistory.getTime()
            remoteId = shoppingHistory.remoteId
            isSynced = false
        }
    }

    fun getShoppingId(): Long {
        return shoppingId;
    }

    fun getDateId(): Long {
        return dateId;
    }

    fun getTime(): Long {
        return time;
    }
    fun setShoppingId(shoppingId: Long){
        this.shoppingId = shoppingId
    }
}