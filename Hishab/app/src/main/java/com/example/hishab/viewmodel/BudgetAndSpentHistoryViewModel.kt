package com.example.hishab.viewmodel

import android.app.Application
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.MonthlySpentModel
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import javax.inject.Inject

@HiltViewModel
class BudgetAndSpentHistoryViewModel@Inject constructor (app: Application) :AndroidViewModel(app)  {


    @Inject
    lateinit var repository:Repository

    fun getPreviousMonthsBudgetAndSpentHistory(year:Int,month:Int): Flowable<List<MonthlySpentModel>> {
       return repository.getPreviousMonthsBudgetAndSpentHistory(year,month)
    }
    fun getBudgetFromMonthAndYear(year:Int,month: Int):Int{
        return repository.getBudgetFromMonthAndYear(year,month)
    }

    fun prepareMultipleViewTypeList(list: List<MonthlySpentModel>):List<Any> {
        var objectList=ArrayList<Any>()
        var lastYear=-1
        list.forEach{
            if(it.getYear()!=lastYear)
            {
                objectList.add(it.getYear().toString())
                lastYear=it.getYear()
            }
            objectList.add(it)
        }
        return objectList
    }
}