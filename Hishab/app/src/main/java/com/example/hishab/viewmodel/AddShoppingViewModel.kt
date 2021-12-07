package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.entities.*
import com.example.hishab.repository.Repository
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
    lateinit var repository:Repository
    lateinit var backUpCategory: Category
    lateinit var backUpShoppingItem: ShoppingItem

    var isUpdating=false




    fun setUpdateField(updatePurchaseHistory: PurchaseHistory?) {
        isUpdating=true;
        if(updatePurchaseHistory!=null)
        {
            updatePurchaseHistory.getPurchaseId()?.let { purchaseItem.setPurchaseId(it) };
            updatePurchaseHistory.getCost()?.let { purchaseItem.setCost(it) };
            updatePurchaseHistory.getItemName()?.let { shoppingItem.setProductName(it) }
            updatePurchaseHistory.getCategoryName()?.let{category.setCategoryName(it)}
        }
    }

    /*suspend fun getProductCategoryList(): List<CategoryAndProductModel> {
        return repository.getProductCategoryList()
    }*/
    fun handleInformationChange()
    {
        //TODO
        //handle category and product mapping
        if(!backUpCategory.getCategoryName().equals(category.getCategoryName()))
        {
            if(backUpCategory.categoryId==category.categoryId)
            {
                category.categoryId=-1
            }
        }
        if(!backUpShoppingItem.getProductName().equals(backUpShoppingItem.getProductName()))
        {
            if(backUpShoppingItem.productId==shoppingItem.productId)
            {
                shoppingItem.productId=-1
            }
        }
    }
}