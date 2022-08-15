package com.example.hishab.retrofit.request;

import com.example.hishab.retrofit.response.ProductResponse

public class ShoppingItem (
        var shoppingItemId : Int,
        var cost:Int,
        var description:String,
        var categoryName:String,
        var localId:Int,
        var productResponse: ProductResponse
){
}
