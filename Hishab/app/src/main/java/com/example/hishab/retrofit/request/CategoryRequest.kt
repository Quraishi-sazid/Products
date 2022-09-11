package com.example.hishab.retrofit.request

import com.example.hishab.retrofit.JsonConverter

class CategoryRequest() : JsonConverter() {
    var categoryId = 0
    var newCategoryName: String? = null
    var oldCategoryName: String? = null
    var localId: Int = 0

    constructor(categoryId: Int, newCategoryName: String?, localId: Int) : this() {
        this.categoryId = categoryId
        this.newCategoryName = newCategoryName
        this.localId = localId
    }

    constructor(categoryId: Int, newCategoryName: String?, oldCategoryName: String?, localId: Int) : this() {
        this.categoryId = categoryId
        this.newCategoryName = newCategoryName
        this.oldCategoryName = oldCategoryName
        this.localId = localId
    }
}