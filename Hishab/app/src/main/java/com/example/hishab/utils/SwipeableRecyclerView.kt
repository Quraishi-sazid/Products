package com.example.hishab.utils

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.PublishSubject

class SwipeableRecyclerView<T>(private val context:Context,private val recyclerView: RecyclerView,private val adpater:RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    init {
        recyclerView.layoutManager=LinearLayoutManager(context)
    }
    private var swipedSubject= PublishSubject.create<Pair<RecyclerView.ViewHolder,Int>>()
    private var dialogClickedSubject=PublishSubject.create<T>()

    fun setSwipeCallback()
    {
        ItemTouchHelper(SwipeToDeleteCallback<T>(context)).attachToRecyclerView(
            recyclerView
        )
    }
    fun getSwipedSubject():PublishSubject<Pair<RecyclerView.ViewHolder,Int>>
    {
        return swipedSubject
    }

}