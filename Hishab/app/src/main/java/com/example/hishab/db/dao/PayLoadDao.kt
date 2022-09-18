package com.example.hishab.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hishab.models.PayLoadQuery
import com.example.hishab.models.entities.PayLoad

@Dao
interface PayLoadDao {
   /*@Insert
   suspend fun insertPayload(payLoad: PayLoad) : Long
   @Query("select group_concat(payload_id, '#4567#') as payloadIds, apiName as apiName, group_concat(payLoad, '#4567#') as allPayload from tbl_payload group by apiName")
   suspend fun getFailedApiCalls(): List<PayLoadQuery>
   @Query("delete from tbl_payload where payload_id =:payLoadId")
   suspend fun deleteById(payLoadId: Long)*/
}