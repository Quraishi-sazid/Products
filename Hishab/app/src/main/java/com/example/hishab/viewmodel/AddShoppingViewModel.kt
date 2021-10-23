package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.ShoppingItem
import com.example.hishab.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddShoppingViewModel @Inject constructor (app:Application) :AndroidViewModel(app)
{
    @Inject
    lateinit var shoppingItem:ShoppingItem
    @Inject
    lateinit var purchaseItem:PurchaseItem
    @Inject
    lateinit var category:Category
    @Inject
    lateinit var repository:ShoppingRepository


    suspend fun insertPurchaseItem() {
        var queriedCategory=repository.getCategoryIdFromName(category.getCategoryName());
        if(queriedCategory==null)
        {
            repository.insertCategory(category);
            category=repository.getCategoryIdFromName(category.getCategoryName());
            queriedCategory=category
        }
        var quariedShoppingItem=repository.getShoppingItemFromNameAndCId(shoppingItem.getItemName(),category.categoryId);
       if(quariedShoppingItem==null)
       {
           shoppingItem.CategoryId=queriedCategory.categoryId
           repository.insertShopping(shoppingItem)
           quariedShoppingItem=repository.getShoppingItemFromNameAndCId(shoppingItem.getItemName(),queriedCategory.categoryId);
       }
        purchaseItem.shoppingId=quariedShoppingItem.itemId
        repository.insertPurchaseItem(purchaseItem)
    }
}