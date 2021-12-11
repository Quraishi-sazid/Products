package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.DateModel
import com.example.hishab.models.entities.CustomDate
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryDetailsDescriptionViewModel @Inject constructor (app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var repostiory: Repository
    suspend fun getdetailsOfCategoryfromDate(categoryId:Int,dateModel: CustomDate):LiveData<List<PurchaseHistory>>
    {
       return repostiory.getdetailsOfCategoryfromDate(categoryId,dateModel);
    }
}