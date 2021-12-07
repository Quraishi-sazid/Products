package com.example.hishab.changedinter

import androidx.recyclerview.widget.RecyclerView

interface ISwipeItemCallback {
    fun onSwipeItem(viewHolder: RecyclerView.ViewHolder, direction: Int)
}