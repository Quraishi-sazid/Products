package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.BuyingHistory
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BuyingHistoryListViewModel @Inject constructor (app: Application) : AndroidViewModel(app){
    @Inject
    lateinit var repository: Repository
    suspend fun getBuyingHistoryLiveData(): LiveData<List<BuyingHistory>>
    {
       return repository.getBuingHistory()
    }
}