package com.example.hishab.retrofit.request

data class ProductRequest(
    val productId: Int,
    val categoryId: Int,
    val categoryName: String,
    val productName: String,
    val userId: Integer
) {
}