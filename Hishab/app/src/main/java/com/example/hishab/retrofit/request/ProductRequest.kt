package com.example.hishab.retrofit.request

import com.example.hishab.retrofit.JsonConverter


class ProductRequest() : JsonConverter() {
    var productId: Int? = null
    var categoryRequest: CategoryRequest? = null
    var productName: String? = null
    var userId: Int? = null
    var productLocalId = 0
    var localId:Int = 0
    val categoryId: Int
        get() = categoryRequest!!.categoryId
    fun getCategoryLocalId(): Int {
        return categoryRequest!!.localId
    }

    constructor(
        productId: Int?,
        categoryRequest: CategoryRequest,
        productName: String,
        userId: Int,
        localId: Int
    ) : this() {
        this.productId = productId
        this.categoryRequest = categoryRequest
        this.productName = productName
        this.userId = userId
        this.localId = localId
    }
}