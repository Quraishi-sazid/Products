package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.ShoppingItem
import com.example.hishab.repository.ShoppingRepository

class AddShoppingViewModel(app:Application) :AndroidViewModel(app) {

    var shoppingItem=ShoppingItem(0,"");
    var purchaseItem=PurchaseItem(0,0);
    var category=Category();

    private val repository=ShoppingRepository(app)

    suspend fun get():List<Category>
    {
        val all = repository.getAll()
        return  all;
    }

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