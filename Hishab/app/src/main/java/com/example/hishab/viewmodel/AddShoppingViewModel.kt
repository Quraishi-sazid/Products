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

    var isUpdating=false


    suspend fun insertPurchaseItem() {
        if(shoppingItem.productId==0)
        {
            if(category.categoryId!=0)
            {
                var quariedShoppingItem = getShoppingItem(category)
                purchaseItem.productId=quariedShoppingItem.productId
            }
            else
            {
                var queriedCategory = getCategory()
                var quariedShoppingItem = getShoppingItem(queriedCategory)
                purchaseItem.productId=quariedShoppingItem.productId
            }
        }
        else
        {
            purchaseItem.productId=shoppingItem.productId
        }
        repository.insertPurchaseItem(purchaseItem)
    }

    private suspend fun getShoppingItem(queriedCategory: Category): ShoppingItem {
        var quariedShoppingItem = repository.getShoppingItemFromNameAndCId(
            shoppingItem.getProductName(),
            category.categoryId
        );
        if (quariedShoppingItem == null) {
            shoppingItem.CategoryId = queriedCategory.categoryId
            repository.insertShopping(shoppingItem)
            quariedShoppingItem = repository.getShoppingItemFromNameAndCId(
                shoppingItem.getProductName(),
                queriedCategory.categoryId
            );
        }
        return quariedShoppingItem
    }


    private suspend fun getCategory(): Category {
        var queriedCategory = repository.getCategoryIdFromName(category.getCategoryName());
        if (queriedCategory == null) {
            repository.insertCategory(category);
            category = repository.getCategoryIdFromName(category.getCategoryName());
            queriedCategory = category
        }
        return queriedCategory
    }

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

    suspend fun updatePurchaseItem(updatePurchaseHistory: PurchaseHistory) {
        var queriedCategory = getCategory()
        var quariedShoppingItem = getShoppingItem(queriedCategory)
        updatePurchaseHistory.getPurchaseId()?.let { purchaseItem.setPurchaseId(it) }
        repository.updatePurchaseItem(quariedShoppingItem,purchaseItem)
    }
    /*suspend fun getProductCategoryList(): List<CategoryAndProductModel> {
        return repository.getProductCategoryList()
    }*/
}