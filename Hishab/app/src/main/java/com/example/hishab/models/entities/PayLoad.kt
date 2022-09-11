package com.example.hishab.models.entities;

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName ="tbl_payload")
public data class PayLoad(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="payload_id")
    var payloadId : Long=0,
    @NonNull
    public var apiName:String,
    public var payLoad:String,
    public var savingTime:Long = System.currentTimeMillis()
) {


}
