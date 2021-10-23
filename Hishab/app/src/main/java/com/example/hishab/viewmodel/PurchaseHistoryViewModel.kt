package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.repository.ShoppingRepository
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(app:Application):AndroidViewModel(app) {
    private lateinit var purchaseHistoryListLiveData: LiveData<List<PurchaseHistory>>
    @Inject
    lateinit var repository:ShoppingRepository

    suspend fun getPurchaseItems(): LiveData<List<PurchaseHistory>>
    {
        return repository.getPurchaseHistory();
    }

    fun convert(list: List<PurchaseHistory>):List<Any> {
        var objectList=ArrayList<Any>()
        var pMonth="";
        for (it in list)
        {
            val month_name= Util.getMonthForInt(it.getMonth())
            if(!pMonth.equals(month_name))
            {
                objectList.add(month_name);
                pMonth=month_name
            }
            objectList.add(it)
        }
        return objectList
    }
}