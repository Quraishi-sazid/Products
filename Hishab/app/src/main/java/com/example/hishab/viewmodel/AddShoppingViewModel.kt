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
            if (shoppingItemProxy.category.categoryId != 0L) {
                var quariedShoppingItem =
                    getShoppingItem(shoppingItemProxy.category, shoppingItemProxy.product)
                shoppingItemProxy.purchaseItem.productId = quariedShoppingItem.productId
            } else {
                var queriedCategory = getCategory(shoppingItemProxy.category)
                var quariedShoppingItem =
                    getShoppingItem(queriedCategory, shoppingItemProxy.product)
                shoppingItemProxy.purchaseItem.productId = quariedShoppingItem.productId
            }
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