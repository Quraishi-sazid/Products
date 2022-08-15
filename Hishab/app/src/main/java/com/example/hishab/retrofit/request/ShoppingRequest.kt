package com.example.hishab.retrofit.request

import java.util.*

data class ShoppingRequest(
    var shoppingId: Int = -1,
    var date: Date,
    var userId: Int,
    var shoppingItemList: List<ShoppingItem>
) {
}