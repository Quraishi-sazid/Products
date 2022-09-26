package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.ProductDetailsModel
import com.example.hishab.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var repository:ProductRepository

    fun getProductDetailsLiveData(id:Long): LiveData<List<ProductDetailsModel>> {
        return repository.getProductDetailsLiveData(id)
    }
}