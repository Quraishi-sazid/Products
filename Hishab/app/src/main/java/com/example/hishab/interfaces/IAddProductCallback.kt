package com.example.hishab.interfaces

import com.example.hishab.models.BuyingItemProxy
import java.io.Serializable

interface IAddProductCallback:Serializable {
    fun onAddedCallback(product:BuyingItemProxy){}
    fun onUpdateCallBack(product:BuyingItemProxy){}
}