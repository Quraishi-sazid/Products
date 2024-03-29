package com.example.hishab.retrofit

class ApiURL {
    companion object{
        const val LOGIN = "home/api/login"
        const val REGISTRATION = "home/api/registration"
        const val CATEGORY_ADD_OR_UPDATE = "category/api/addOrUpdate"
        const val PRODUCT_ADD_OR_UPDATE = "product/api/addOrUpdateProduct"
        const val CATEGORYLIST_ADD_OR_UPDATE = "category/api/addOrUpdateList"
        const val PRODUCTLIST_ADD_OR_UPDATE = "product/api/addOrUpdateProductList"
        const val SHOPPING_ADD = "/shopping/api/addShopping"
        const val SHOPPING_UPDATE = "/shopping/api/updateShopping"
        const val SHOPPING_ADD_LIST = "/shopping/api/addShoppingList"
        const val SHOPPING_UPDATE_LIST = "/shopping/api/updateShoppingList"
        const val ADD_OR_UPDATE_BUDGET = "/budget/api/addOrUpdateBudget"
        const val ADD_OR_UPDATE_BUDGET_List = "/budget/api/addOrUpdateBudgetList"
        const val GET_CATEGORY_PRODUCT_HISTORY = "/category/api/getCategoryProductHistory"
        const val GET_BUDGET_HISTORY = "/budget/api/getBudgetHistory"
        const val GET_SHOPPING_HISTORY = "/shopping/api/getShoppingHistory"
    }
}