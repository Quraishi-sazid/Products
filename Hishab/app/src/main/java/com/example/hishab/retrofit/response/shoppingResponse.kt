package com.example.hishab.retrofit.response


import com.example.hishab.retrofit.response.ShoppingItemResponse

class shoppingResponse {
    var shoppingId = 0
    var localId = 0L
    var itemResponses: List<ShoppingItemResponse>? = null

    constructor() {}
    constructor(shoppingId: Int, itemResponses: List<ShoppingItemResponse>?) {
        this.shoppingId = shoppingId
        this.itemResponses = itemResponses
    }
}