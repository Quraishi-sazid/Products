package com.example.hishab.retrofit.request

import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.Product
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.retrofit.JsonConverter


class ShoppingItemRequest() : JsonConverter() {
    var shoppingItemId = 0
    var cost = 0
    var descString: String? = null
    var localId = 0L
    var productRequest: ProductRequest? = null
    val categoryId: Int
        get() = productRequest!!.categoryId
    val categoryLocalId: Int
        get() = productRequest!!.getCategoryLocalId()
    val productId: Int
        get() = productRequest!!.productId!!

    constructor(
        shoppingItemId: Int,
        cost: Int,
        descString: String?,
        localId: Long,
        productRequest: ProductRequest?
    ) : this() {
        this.shoppingItemId = shoppingItemId
        this.cost = cost
        this.descString = descString
        this.localId = localId
        this.productRequest = productRequest
    }

    constructor(shoppingItemProxy: ShoppingItemProxy) : this() {
        shoppingItemId = shoppingItemProxy.purchaseItem.remoteId
        localId = shoppingItemProxy.purchaseItem.shoppingId
        cost = shoppingItemProxy.purchaseItem.getCost()
        descString = shoppingItemProxy.purchaseItem.getDescription()
        productRequest = ProductRequest(shoppingItemProxy.product, shoppingItemProxy.category)
    }

    constructor(purchaseItem: PurchaseItem, product: Product, category: Category) : this() {
        shoppingItemId = purchaseItem.remoteId
        cost = purchaseItem.getCost()
        descString = purchaseItem.getDescription()
        localId = purchaseItem.getPurchaseId()
        productRequest = ProductRequest(product, category)
    }
}