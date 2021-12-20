package com.example.hishab.models

import com.example.hishab.models.entities.Category

data class CategoryProxy(var proxyId:Int,var categoryId:Long,var categoryName:String,var totalProductMappedWithThis:Int) {
    fun getCategory(): Category {
        var category=Category()
        category.categoryId=categoryId
        category.setCategoryName(categoryName)
        return category
    }

}