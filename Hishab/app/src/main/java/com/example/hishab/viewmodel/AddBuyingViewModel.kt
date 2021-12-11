package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.BuyingItemProxy
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
        product: Product
    ): Product {

        var quariedShoppingItem = repository.getShoppingItemFromNameAndCId(
            product.getProductName(),
            category.categoryId
        )
        if (quariedShoppingItem == null) {
            product.categoryId = category.categoryId
            product.productId = repository.insertShopping(product)
            return product
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
        buyingItemProxyList: ArrayList<BuyingItemProxy>,
        customDate: CustomDate,
        time: Long
    ) {
        dateId = repository.getDateId(customDate)
        buyingId =
            repository.insertBuyingItem(BuyItem(dateId, time, buyingItemProxyList.size.toLong()))
        buyingItemProxyList.forEach {
            it.purchaseItem.buyingId = buyingId
            insertOrUpdatePurchaseItem(it)
        }
    }


    suspend fun insertOrUpdatePurchaseItem(buyingItemProxy: BuyingItemProxy) {
        if (buyingItemProxy.product.productId == 0L) {
            if (buyingItemProxy.category.categoryId != 0L) {
                var quariedShoppingItem =
                    getShoppingItem(buyingItemProxy.category, buyingItemProxy.product)
                buyingItemProxy.purchaseItem.productId = quariedShoppingItem.productId
            } else {
                var queriedCategory = getCategory(buyingItemProxy.category)
                var quariedShoppingItem =
                    getShoppingItem(queriedCategory, buyingItemProxy.product)
                buyingItemProxy.purchaseItem.productId = quariedShoppingItem.productId
            }
        } else {
            buyingItemProxy.purchaseItem.productId = buyingItemProxy.product.productId
        }
        if (!buyingItemProxy.isUpdating())
            repository.insertPurchaseItem(buyingItemProxy.purchaseItem)
        else
            repository.updatePurchaseItem(buyingItemProxy.purchaseItem)
    }

    suspend fun getPurchaseHistoryByBuyingId(buyingId: Long): List<PurchaseHistory> {
        return repository.getPurchaseHistoryFromBuyingId(buyingId)
    }

    suspend fun updateBuying(dataSource: ArrayList<BuyingItemProxy>, buyingDate: CustomDate) {
        dateId = repository.getDateId(buyingDate)
        repository.updateDateId(dateId, buyingId);
        dataSource.forEach { insertOrUpdatePurchaseItem(it) }
    }

    suspend fun deletePurchaseItem(purchaseItem: PurchaseItem) {
        repository.deletePurchaseHistory(purchaseItem.getPurchaseId())
    }

}