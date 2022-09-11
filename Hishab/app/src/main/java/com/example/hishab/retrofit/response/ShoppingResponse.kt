package com.example.hishab.retrofit.response


import com.example.hishab.retrofit.response.ShoppingItemResponse

class ShoppingResponse {
    var shoppingId = 0
    var itemResponses: List<ShoppingItemResponse>? = null

    constructor() {}
    constructor(shoppingId: Int, itemResponses: List<ShoppingItemResponse>?) {
        this.shoppingId = shoppingId
        this.itemResponses = itemResponses
    }
}