package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.entities.Budget
import com.example.hishab.models.entities.Product
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBudgetViewModel @Inject constructor (app: Application) : AndroidViewModel(app) {
 /*   @Inject//hilt not instantiating.need to check
    lateinit*/ var repository= Repository(app)

    fun updateBudgetList(budgetList:List<Budget>){
        repository.updateBudgetList(budgetList);
    }
}