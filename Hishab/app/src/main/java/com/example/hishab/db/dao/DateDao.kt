package com.example.hishab.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hishab.models.entities.CustomDate

@Dao
interface DateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(date:CustomDate):Long
    @Query("Select * from custom_date where day=:pDay and month=:pMonth and year=:pYear ")
    suspend fun getDateId(pDay:Int,pMonth:Int,pYear:Int):CustomDate
    @Insert
    abstract fun insertCustomDate(vararg customDate: CustomDate)
}