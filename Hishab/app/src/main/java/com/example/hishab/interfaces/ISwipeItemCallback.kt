package com.example.hishab.interfaces

import androidx.recyclerview.widget.RecyclerView

interface ISwipeItemCallback {
    fun onSwipeItem(viewHolder: RecyclerView.ViewHolder, direction: Int)
}