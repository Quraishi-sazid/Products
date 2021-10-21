package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(app:Application):AndroidViewModel(app) {
    lateinit var purchaseHistoryList:List<PurchaseHistory>
    @Inject
    lateinit var repository:ShoppingRepository

    suspend fun getPurchaseItems()
    {
        purchaseHistoryList = repository.getPurchaseHistory();
    }
}