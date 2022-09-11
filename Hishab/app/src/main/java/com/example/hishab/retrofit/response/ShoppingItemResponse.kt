package com.example.hishab.retrofit.response

import com.example.hishab.retrofit.response.ProductResponse

class ShoppingItemResponse {
    var localId = 0
    var shoppingItemId = 0
    var productResponse: ProductResponse? = null

    constructor() {}
    constructor(localId: Int, shoppingItemId: Int, productResponse: ProductResponse?) {
        this.localId = localId
        this.shoppingItemId = shoppingItemId
        this.productResponse = productResponse
    }
}