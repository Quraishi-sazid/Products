package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.DateModel
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.repository.Repository
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(app:Application):AndroidViewModel(app) {
    private lateinit var purchaseHistoryListLiveData: LiveData<List<PurchaseHistory>>
    @Inject
    lateinit var repository:Repository

    suspend fun getPurchaseItems(): LiveData<List<PurchaseHistory>>
    {
        return repository.getPurchaseHistory();
    }

    suspend fun getdetailsOfCategoryfromDate(categoryId:Int,dateModel: DateModel):LiveData<List<PurchaseHistory>>
    {
        return repository.getdetailsOfCategoryfromDate(categoryId,dateModel);
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

    suspend fun delete(position:Int) {
        repository.deletePurchaseHistory(position);
    }
}