package com.example.hishab.changedinter

import com.example.hishab.models.AddItemProxy
import java.io.Serializable

interface IAddProductCallback:Serializable {
    fun onAddedCallback(product:AddItemProxy){}
    fun onUpdateCallBack(product:AddItemProxy){}
}