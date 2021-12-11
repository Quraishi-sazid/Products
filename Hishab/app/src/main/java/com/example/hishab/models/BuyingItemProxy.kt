package com.example.hishab.models

import androidx.databinding.BaseObservable
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.Product
import java.io.Serializable

data class BuyingItemProxy(public var proxyId:Long, public val category: Category, public val product: Product, public val purchaseItem: PurchaseItem):Serializable, BaseObservable() {
    fun isUpdating():Boolean
    {
        return purchaseItem.getPurchaseId()!=0L
    }

    fun setBuyingId(buyingId: Long) {
        purchaseItem.buyingId=buyingId
    }
}