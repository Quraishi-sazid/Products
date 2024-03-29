package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.models.ShoppingHistory
import com.example.hishab.models.entities.*
import com.example.hishab.repository.ProductRepository
import com.example.hishab.repository.ShoppingRepository
import com.example.hishab.retrofit.request.ShoppingItemRequest
import com.example.hishab.retrofit.request.ShoppingRequest
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class AddShoppingViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    var updatingShoppingHistory: ShoppingHistory? = null
    var isUpdating: Boolean = false

    @Inject
    lateinit var shoppingRepository: ShoppingRepository

    @Inject
    lateinit var productRepository: ProductRepository
    var buyingId: Long = -1
    var time: Long = -1
    var productId: Long = -1
    var dateId = -1L
    fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return productRepository.getProductCategoryListLeftJoin()
    }

    suspend fun insertBuying(
        shoppingItemProxyList: ArrayList<ShoppingItemProxy>,
        customDate: CustomDate,
        time: Long
    ) {
        dateId = shoppingRepository.getDateId(customDate)
        var shopping = Shopping(dateId, time)
        shopping.setShoppingId(shoppingRepository.insertBuyingItem(shopping))
        shoppingItemProxyList.forEach {
            it.purchaseItem.shoppingId = shopping.getShoppingId()
            insertOrUpdatePurchaseItem(it)
        }
        var shoppingItemRequestList = ArrayList<ShoppingItemRequest>()
        shoppingItemProxyList.forEach { shoppingItemRequestList.add(ShoppingItemRequest(it)) }
        GlobalScope.launch {
            shoppingRepository.saveShoppingRequestToRemote(
                ShoppingRequest(
                    shopping,
                    shoppingItemRequestList
                ),false
            )
        }

    }

    private suspend fun insertOrUpdatePurchaseItem(shoppingItemProxy: ShoppingItemProxy) {
        if (shoppingItemProxy.product.productId == 0L) {
            var product =
                productRepository.getProductIdByInsertingInDataBase(
                    shoppingItemProxy.getCategoryId(),
                    shoppingItemProxy.getCategoryName(),
                    shoppingItemProxy.getProductName()
                )
            shoppingItemProxy.purchaseItem.productId = product.productId
            shoppingItemProxy.category.categoryId = product.categoryId
            shoppingItemProxy.product.productId = product.productId
        } else {
            shoppingItemProxy.purchaseItem.productId = shoppingItemProxy.product.productId
        }
        if (!shoppingItemProxy.isUpdating())
            shoppingRepository.insertPurchaseItem(shoppingItemProxy.purchaseItem)
        else
            shoppingRepository.updatePurchaseItem(shoppingItemProxy.purchaseItem)
    }

    suspend fun getPurchaseHistoryByBuyingId(buyingId: Long): List<PurchaseHistory> {
        return shoppingRepository.getPurchaseHistoryFromBuyingId(buyingId)
    }

    suspend fun updateBuying(
        dataSource: ArrayList<ShoppingItemProxy>,
        buyingDate: CustomDate,
        milisec: Long
    ) {
        dateId = shoppingRepository.getDateId(buyingDate)
        shoppingRepository.updateDateId(dateId, buyingId)
        shoppingRepository.updateTimeForShopping(milisec, buyingId)
        dataSource.forEach { insertOrUpdatePurchaseItem(it) }
        var shoppingItemRequests = ArrayList<ShoppingItemRequest>()
        dataSource.forEach { shoppingItemRequests.add(ShoppingItemRequest(it)) }
        GlobalScope.launch {
            shoppingRepository.saveShoppingRequestToRemote(
                ShoppingRequest(
                    Shopping(
                        dateId,
                        updatingShoppingHistory
                    ), shoppingItemRequests
                ),true
            )
        }

    }

    suspend fun deletePurchaseItem(purchaseItem: PurchaseItem) {
        shoppingRepository.deletePurchaseHistory(purchaseItem.getPurchaseId())
    }

    fun getTimePickerHour(): Int {
        if (isUpdating) {
            return Util.getTimePickerHour(time)
        }
        return Util.getTimePickerHour(System.currentTimeMillis())
    }

    fun getTimePickerMin(): Int {
        if (isUpdating) {
            return Util.getTimePickerMin(time)
        }
        return Util.getTimePickerMin(System.currentTimeMillis())
    }
}