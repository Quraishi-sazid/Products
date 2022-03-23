package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.entities.CustomDate
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.repository.Repository
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import javax.inject.Inject
@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(app:Application):AndroidViewModel(app) {
    private lateinit var purchaseHistoryListLiveData: LiveData<List<PurchaseHistory>>
    @Inject
    lateinit var repository:Repository

    fun getPurchaseItems(): Observable<List<PurchaseHistory>>
    {
        return repository.getPurchaseHistory();
    }

    fun getdetailsOfCategoryfromDate(categoryId:Int,dateModel: CustomDate) :Observable<List<PurchaseHistory>>
    {
        return repository.getDetailsOfCategoryfromDate(categoryId,dateModel)
    }


    fun getDateSeparatedPurchaseHistoryList(list: List<PurchaseHistory>):List<Any> {
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

    suspend fun delete(position:Long) {
        repository.deletePurchaseHistory(position);
    }

    suspend fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return repository.getProductCategoryListLeftJoin()
    }
}