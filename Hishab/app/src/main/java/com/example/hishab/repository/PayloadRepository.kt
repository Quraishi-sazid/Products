package com.example.hishab.repository

import android.app.Application
import android.content.Context
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.PayLoadDao
import com.example.hishab.models.PayLoadQuery
import com.example.hishab.models.entities.PayLoad
import dagger.hilt.EntryPoints

class PayloadRepository(context: Context) {
    var database = AppDatabase.getDatabase(context)
    var payLoadDao : PayLoadDao
    init {
        payLoadDao = database.paylodDao()
    }
    /*suspend fun getPayLoadQuery():List<PayLoadQuery>{
        return payLoadDao.getFailedApiCalls()
    }
    suspend fun deleteFromPayloadById(payloadId:Long){
        payLoadDao.deleteById(payloadId)
    }

    suspend fun InsertIntoPayload(payLoad: PayLoad):Long {
       return payLoadDao.insertPayload(payLoad)
    }*/
}