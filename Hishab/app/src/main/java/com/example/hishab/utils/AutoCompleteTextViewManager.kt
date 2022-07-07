package com.example.hishab.utils

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import io.reactivex.Observable


class AutoCompleteTextViewManager<T>(val context: Context, val autoCompleteTextView: AutoCompleteTextView, private val dataList:List<T>) {
    var selectedItem:T? =null

    var type=android.R.layout.simple_dropdown_item_1line
    var onAutoCompleteSelectionChanged: Observable<T>

    init {
        autoCompleteTextView.setAdapter(ArrayAdapter<T>(context,android.R.layout.simple_dropdown_item_1line,dataList))
        onAutoCompleteSelectionChanged=Observable.create<T>{ emitter->
            autoCompleteTextView.onItemClickListener=AdapterView.OnItemClickListener() { adapterView: AdapterView<*>, view1: View, position: Int, id: Long ->
                selectedItem = adapterView.adapter.getItem(position) as T
                emitter.onNext(selectedItem!!)
            }
        }
    }

}