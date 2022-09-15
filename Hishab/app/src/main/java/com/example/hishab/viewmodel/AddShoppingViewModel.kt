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
import com.example.hishab.repository.Repository
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class AddShoppingViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    var updatingShoppingHistory: ShoppingHistory? = null
    var isUpdating: Boolean = false

    @Inject
    lateinit var repository: Repository
    @Inject
    lateinit var productRepository: ProductRepository
    var buyingId: Long = -1
    var time: Long = -1
    var productId: Long = -1
    var dateId = -1L;
    fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return productRepository.getProductCategoryListLeftJoin()
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
            shoppingItemProxy.purchaseItem.productId = productRepository.getProductIdByInsertingInDataBase(
                shoppingItemProxy.getCategoryId(),
                shoppingItemProxy.getCategoryName(),
                shoppingItemProxy.getProductName()
            ).productId
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

    suspend fun updateBuying(dataSource: ArrayList<ShoppingItemProxy>, buyingDate: CustomDate, milisec: Long) {
        dateId = repository.getDateId(buyingDate)
        repository.updateDateId(dateId, buyingId)
        repository.updateTimeForShopping(milisec,buyingId)
        dataSource.forEach { insertOrUpdatePurchaseItem(it) }
    }

    suspend fun deletePurchaseItem(purchaseItem: PurchaseItem) {
        repository.deletePurchaseHistory(purchaseItem.getPurchaseId())
    }

    fun getTimePickerHour(): Int {
        if(isUpdating){
            return Util.getTimePickerHour(time)
        }
        return Util.getTimePickerHour(System.currentTimeMillis())
    }

    fun getTimePickerMin(): Int {
        if(isUpdating){
            return Util.getTimePickerMin(time)
        }
        return Util.getTimePickerMin(System.currentTimeMillis())
    }



}