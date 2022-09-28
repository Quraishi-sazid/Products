package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.*
import com.example.hishab.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPurchaseItemViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    @Inject
    lateinit var product: Product

    @Inject
    lateinit var purchaseItem: PurchaseItem

    @Inject
    lateinit var category: Category

    @Inject
    lateinit var shoppingRepository: ShoppingRepository
    lateinit var backUpCategory: Category
    lateinit var backUpProduct: Product

    var isUpdating = false

    fun setUpdateField(updatePurchaseHistory: PurchaseHistory?) {
        isUpdating = true;
        if (updatePurchaseHistory != null) {
            updatePurchaseHistory.getPurchaseId()?.let { purchaseItem.setPurchaseId(it) };
            updatePurchaseHistory.getCost()?.let { purchaseItem.setCost(it) };
            updatePurchaseHistory.getItemName()?.let { product.setProductName(it) }
            updatePurchaseHistory.getCategoryName()?.let { category.setCategoryName(it) }
        }
    }

    fun handleInformationChange() {
        //TODO
        //handle category and product mapping
        if (!backUpCategory.getCategoryName().equals(category.getCategoryName())) {
            if (backUpCategory.categoryId == category.categoryId) {
                category.categoryId = 0L
            }
        }
        if (!backUpProduct.getProductName().equals(product.getProductName())) {
            if (backUpProduct.productId == product.productId) {
                product.productId = 0L
            }
        }
    }

    fun setValuesForUpdating(shoppingItemProxy: ShoppingItemProxy) {
        isUpdating = true
        product = shoppingItemProxy.product
        category = shoppingItemProxy.category
        purchaseItem = shoppingItemProxy.purchaseItem
        backUpCategory = category.deepCopy()
        backUpProduct = product.deepCopy()
    }
}