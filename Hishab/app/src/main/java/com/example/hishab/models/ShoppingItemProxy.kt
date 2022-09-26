package com.example.hishab.models

import androidx.databinding.BaseObservable
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.Product
import java.io.Serializable

data class ShoppingItemProxy(var proxyId:Long,val category: Category,val product: Product, val purchaseItem: PurchaseItem):Serializable, BaseObservable() {


    fun isUpdating():Boolean
    {
        return purchaseItem.getPurchaseId()!=0L
    }

    fun setBuyingId(buyingId: Long) {
        purchaseItem.shoppingId=buyingId
    }
    fun getCategoryId():Long{
        return category.categoryId
    }
    fun getProductId():Long{
        return product.productId
    }
    fun getCategoryName():String{
        return category.getCategoryName()
    }
    fun getProductName():String{
        return product.getProductName()
    }
}