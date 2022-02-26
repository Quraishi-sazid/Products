package com.example.hishab.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.hishab.interfaces.IAutoCompleteClickedCallback
import com.example.hishab.models.CategoryAndProductModel


class AutoCompleteTextViewManager<T>(val context: Context, val autoCompleteTextView: AutoCompleteTextView, private val dataList:List<T>) {
    var selectedItem:T? =null

    var type=android.R.layout.simple_dropdown_item_1line

    fun setAdapter()
    {
        autoCompleteTextView.setAdapter(ArrayAdapter<T>(context,android.R.layout.simple_dropdown_item_1line,dataList))
    }
    fun setCallBack(iAutoCompleteClickedCallback: IAutoCompleteClickedCallback<T>?=null)
    {
        AdapterView.OnItemClickListener() { adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
            selectedItem = adapterView.adapter.getItem(position) as T
            iAutoCompleteClickedCallback?.onAutoCompleteClickedCallback(selectedItem)
        }
    }

}