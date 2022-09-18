package com.example.hishab.retrofit.request

import com.example.hishab.models.entities.Category
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

    constructor(category: Category) : this() {
        categoryId =-1
        localId = category.categoryId.toInt()
        newCategoryName = category.getCategoryName()
    }
}