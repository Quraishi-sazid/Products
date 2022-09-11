package com.example.hishab.retrofit.request

import com.example.hishab.retrofit.JsonConverter


class ShoppingItemRequest() : JsonConverter() {
    var shoppingItemId = 0
    var cost = 0
    var descString: String? = null
    var localId = 0
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
        localId: Int,
        productRequest: ProductRequest?
    ) : this() {
        this.shoppingItemId = shoppingItemId
        this.cost = cost
        this.descString = descString
        this.localId = localId
        this.productRequest = productRequest
    }
}