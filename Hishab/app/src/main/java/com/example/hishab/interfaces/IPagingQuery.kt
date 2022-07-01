package com.example.hishab.interfaces

import androidx.paging.PagingSource
import com.example.hishab.models.PagingQueryModel
import com.example.hishab.models.PurchaseHistory

interface IPagingQuery<T : Any> {
    suspend fun getPagingData(lastId:T?,loadSize:Int): PagingQueryModel<T>
}