package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.AddItemProxy
import com.example.hishab.models.entities.*
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddBuyingViewModel @Inject constructor (app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var repository: Repository
    var buyingId:Long=-1
    var dateId=-1L;
    suspend fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return repository.getProductCategoryList()
    }

    private suspend fun getShoppingItem(
        category: Category,
        shoppingItem: ShoppingItem
    ): ShoppingItem {

        var quariedShoppingItem = repository.getShoppingItemFromNameAndCId(shoppingItem.getProductName(),category.categoryId)
        if (quariedShoppingItem == null) {
            shoppingItem.CategoryId = category.categoryId
            shoppingItem.productId = repository.insertShopping(shoppingItem)
            return shoppingItem
        }
        System.out.println("quariedShoppingItem id="+quariedShoppingItem.productId)
        return quariedShoppingItem
    }

    private suspend fun getCategory(category: Category): Category {
        var queriedCategory = repository.getCategoryIdFromName(category.getCategoryName());
        if (queriedCategory == null) {
            category.categoryId = repository.insertCategory(category)
            return category
        }
        return queriedCategory
    }

    suspend fun insertShopping(addItemProxyList:ArrayList<AddItemProxy>,customDate: CustomDate,time:Long)
    {
        dateId=repository.getDateId(customDate)
        buyingId=repository.insertBuyingItem(BuyItem(dateId,time,addItemProxyList.size))
        addItemProxyList.forEach{insertPurchaseItem(it)}
    }


    suspend fun insertPurchaseItem(addItemProxy: AddItemProxy) {
        //setBuyingId
        if(buyingId==-1L)
        {
            buyingId=repository.getLatestBuyingId();
        }
        addItemProxy.purchaseItem.buyingId=buyingId
        if(addItemProxy.shoppingItem.productId==0L)
        {
            if(addItemProxy.category.categoryId!=0L)
            {
                var quariedShoppingItem = getShoppingItem(addItemProxy.category,addItemProxy.shoppingItem)
                addItemProxy.purchaseItem.productId=quariedShoppingItem.productId
            }
            else
            {
                var queriedCategory = getCategory(addItemProxy.category)
                var quariedShoppingItem = getShoppingItem(queriedCategory,addItemProxy.shoppingItem)
                addItemProxy.purchaseItem.productId=quariedShoppingItem.productId
            }
        }
        else
        {
            addItemProxy.purchaseItem.productId=addItemProxy.shoppingItem.productId
        }
        repository.insertPurchaseItem(addItemProxy.purchaseItem)
    }

}