package com.example.hishab.interfaces

import com.example.hishab.models.ShoppingItemProxy
import java.io.Serializable

interface IAddOrUpdateProductCallback:Serializable {
    fun onAddedCallback(product:ShoppingItemProxy){}
    fun onUpdateCallBack(product:ShoppingItemProxy){}
}