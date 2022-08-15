package com.example.hishab.retrofit.commonmodel

data class CategoryRequestResponse(
    val categoryId: Int,
    val newCategoryName: String,
    val oldCategoryName: String
) {
}