package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.models.entities.*
import com.example.hishab.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddShoppingViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    var isUpdating: Boolean = false

    @Inject
    lateinit var repository: Repository
    var buyingId: Long = -1
    var dateId = -1L;
    suspend fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return repository.getProductCategoryListLeftJoin()
    }

    suspend fun insertBuying(
        shoppingItemProxyList: ArrayList<ShoppingItemProxy>,
        customDate: CustomDate,
        time: Long
    ) {
        dateId = repository.getDateId(customDate)
        buyingId =
            repository.insertBuyingItem(Shopping(dateId, time))
        shoppingItemProxyList.forEach {
            it.purchaseItem.shoppingId = buyingId
            insertOrUpdatePurchaseItem(it)
        }
    }

    suspend fun insertOrUpdatePurchaseItem(shoppingItemProxy: ShoppingItemProxy) {
        if (shoppingItemProxy.product.productId == 0L) {
            shoppingItemProxy.purchaseItem.productId = repository.getProductIdByInsertingInDataBase(
                shoppingItemProxy.getCategoryId(),
                shoppingItemProxy.getCategoryName(),
                shoppingItemProxy.getProductName()
            )
        } else {
            shoppingItemProxy.purchaseItem.productId = shoppingItemProxy.product.productId
        }
        if (!shoppingItemProxy.isUpdating())
            repository.insertPurchaseItem(shoppingItemProxy.purchaseItem)
        else
            repository.updatePurchaseItem(shoppingItemProxy.purchaseItem)
    }

    suspend fun getPurchaseHistoryByBuyingId(buyingId: Long): List<PurchaseHistory> {
        return repository.getPurchaseHistoryFromBuyingId(buyingId)
    }

    suspend fun updateBuying(dataSource: ArrayList<ShoppingItemProxy>, buyingDate: CustomDate) {
        dateId = repository.getDateId(buyingDate)
        repository.updateDateId(dateId, buyingId);
        dataSource.forEach { insertOrUpdatePurchaseItem(it) }
    }

    suspend fun deletePurchaseItem(purchaseItem: PurchaseItem) {
        repository.deletePurchaseHistory(purchaseItem.getPurchaseId())
    }

}