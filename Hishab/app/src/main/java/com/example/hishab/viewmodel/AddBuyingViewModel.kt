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
class AddBuyingViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    var isUpdating: Boolean = false

    @Inject
    lateinit var repository: Repository
    var buyingId: Long = -1
    var dateId = -1L;
    suspend fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return repository.getProductCategoryList()
    }

    private suspend fun getShoppingItem(
        category: Category,
        shoppingItem: ShoppingItem
    ): ShoppingItem {

        var quariedShoppingItem = repository.getShoppingItemFromNameAndCId(
            shoppingItem.getProductName(),
            category.categoryId
        )
        if (quariedShoppingItem == null) {
            shoppingItem.CategoryId = category.categoryId
            shoppingItem.productId = repository.insertShopping(shoppingItem)
            return shoppingItem
        }
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

    suspend fun insertBuying(
        addItemProxyList: ArrayList<AddItemProxy>,
        customDate: CustomDate,
        time: Long
    ) {
        dateId = repository.getDateId(customDate)
        buyingId =
            repository.insertBuyingItem(BuyItem(dateId, time, addItemProxyList.size.toLong()))
        addItemProxyList.forEach {
            it.purchaseItem.buyingId = buyingId
            insertOrUpdatePurchaseItem(it)
        }
    }


    suspend fun insertOrUpdatePurchaseItem(addItemProxy: AddItemProxy) {
        if (addItemProxy.shoppingItem.productId == 0L) {
            if (addItemProxy.category.categoryId != 0L) {
                var quariedShoppingItem =
                    getShoppingItem(addItemProxy.category, addItemProxy.shoppingItem)
                addItemProxy.purchaseItem.productId = quariedShoppingItem.productId
            } else {
                var queriedCategory = getCategory(addItemProxy.category)
                var quariedShoppingItem =
                    getShoppingItem(queriedCategory, addItemProxy.shoppingItem)
                addItemProxy.purchaseItem.productId = quariedShoppingItem.productId
            }
        } else {
            addItemProxy.purchaseItem.productId = addItemProxy.shoppingItem.productId
        }
        if (!addItemProxy.isUpdating())
            repository.insertPurchaseItem(addItemProxy.purchaseItem)
        else
            repository.updatePurchaseItem(addItemProxy.purchaseItem)
    }

    suspend fun getPurchaseHistoryByBuyingId(buyingId: Long): List<PurchaseHistory> {
        return repository.getPurchaseHistoryFromBuyingId(buyingId)
    }

    suspend fun updateBuying(dataSource: ArrayList<AddItemProxy>, buyingDate: CustomDate) {
        dateId = repository.getDateId(buyingDate)
        repository.updateDateId(dateId, buyingId);
        dataSource.forEach { insertOrUpdatePurchaseItem(it) }
    }

    suspend fun deletePurchaseItem(purchaseItem: PurchaseItem) {
        repository.deletePurchaseHistory(purchaseItem.getPurchaseId())
    }

}