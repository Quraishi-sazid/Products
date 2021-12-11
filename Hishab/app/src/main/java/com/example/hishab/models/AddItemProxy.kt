package com.example.hishab.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.hishab.BR
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.ShoppingItem
import java.io.Serializable

data class AddItemProxy(public var proxyId:Long, public val category: Category, public val shoppingItem: ShoppingItem, public val purchaseItem: PurchaseItem):Serializable, BaseObservable() {
    fun isUpdating():Boolean
    {
        return purchaseItem.getPurchaseId()!=0L
    }

    fun setBuyingId(buyingId: Long) {
        purchaseItem.buyingId=buyingId
    }
}