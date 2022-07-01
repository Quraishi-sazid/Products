package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.hishab.adapter.ShoppingHistoryAdapter
import com.example.hishab.models.ShoppingHistory
import com.example.hishab.repository.Repository
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingHistoryViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    var xx = 4
    @Inject
    lateinit var repository: Repository
    fun getBuyingHistoryLiveData(): LiveData<List<ShoppingHistory>> {
        return repository.getBuingHistory()
    }

    fun processData(list: List<ShoppingHistory>): List<Any> {
        var prevYear = -1
        var prevMonth = -1
        var returnList = ArrayList<Any>()
        list.forEach {
            if (it.getYear() != prevYear) {
                returnList.add(it.getYear())
                returnList.add(Util.getMonthForInt(it.getMonth()))
                prevYear = it.getYear()
                prevMonth = it.getMonth()
            } else if (it.getMonth() != prevMonth) {
                returnList.add(Util.getMonthForInt(it.getMonth()))
                prevMonth = it.getMonth()
            }
            returnList.add(it)
        }
        return returnList
    }

}
