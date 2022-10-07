package com.example.hishab.retrofit.request

import com.example.hishab.models.entities.Shopping
import com.example.hishab.retrofit.JsonConverter
import com.example.hishab.retrofit.request.ShoppingItemRequest
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper
import com.example.hishab.utils.Util
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ShoppingRequest() : JsonConverter() {
    var shoppingId = 0
    var userId = 0
    var date: String = ""
    var localId = 0L
    var time:Long?=0L
    var shoppingItemRequests: List<ShoppingItemRequest>? = null

    constructor(
        shoppingId: Int,
        userId: Int,
        date: Date?,
        shoppingItemRequests: List<ShoppingItemRequest>?
    ) : this() {
        this.shoppingId = shoppingId
        this.userId = userId
        this.date = SimpleDateFormat("yyyy-MM-dd").format(date)
        this.time = date?.time
        this.shoppingItemRequests = shoppingItemRequests
    }

    constructor(
        shopping: Shopping,
        shoppingItemRequestList: ArrayList<ShoppingItemRequest>
    ) : this() {
        shoppingId = shopping.remoteId.toInt()
        localId = shopping.getShoppingId()
        userId = PreferenceHelper.get(Constant.User_Id, -1)
        this.date = SimpleDateFormat("yyyy-MM-dd").format(Date(shopping.getTime()))
        this.time = shopping.getTime()
        shoppingItemRequests = shoppingItemRequestList
    }
}