package com.example.hishab.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable


interface IViewPagerSwipeListener {
    var onSwipedRightOrLeft: Observable<Pair<Float,Float>>
}