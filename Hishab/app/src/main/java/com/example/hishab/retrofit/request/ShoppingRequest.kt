package com.example.hishab.retrofit.request

import com.example.hishab.retrofit.JsonConverter
import com.example.hishab.retrofit.request.ShoppingItemRequest
import java.sql.Date

class ShoppingRequest() : JsonConverter() {
    var shoppingId = 0
    var userId = 0
    var date: Date? = null
    var shoppingItemRequests: List<ShoppingItemRequest>? = null

    constructor(
        shoppingId: Int,
        userId: Int,
        date: Date?,
        shoppingItemRequests: List<ShoppingItemRequest>?
    ) : this() {
        this.shoppingId = shoppingId
        this.userId = userId
        this.date = date
        this.shoppingItemRequests = shoppingItemRequests
    }
}