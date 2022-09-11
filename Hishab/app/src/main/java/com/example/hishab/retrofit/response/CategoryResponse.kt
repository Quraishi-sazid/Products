package com.example.hishab.retrofit.response

class CategoryResponse {
    var localId = 0
    var categoryId = 0

    constructor() {}
    constructor(localId: Int, categoryId: Int) {
        this.localId = localId
        this.categoryId = categoryId
    }
}