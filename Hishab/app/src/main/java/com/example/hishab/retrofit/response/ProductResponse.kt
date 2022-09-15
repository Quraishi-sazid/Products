package com.example.hishab.retrofit.response


import com.example.hishab.models.entities.Product
import com.example.hishab.retrofit.response.CategoryResponse

class ProductResponse() {
    var productId = 0
    var productName = "";
    var localId = 0
    var categoryResponse: CategoryResponse? = null

}