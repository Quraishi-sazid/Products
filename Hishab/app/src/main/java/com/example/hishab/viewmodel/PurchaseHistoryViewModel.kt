package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.repository.ShoppingRepository

class PurchaseHistoryViewModel(app:Application):AndroidViewModel(app) {
    lateinit var purchaseHistoryList:List<PurchaseHistory>
    private val repository= ShoppingRepository(app)

    suspend fun getPurchaseItems() {
        purchaseHistoryList = repository.getPurchaseHistory();
    }
}