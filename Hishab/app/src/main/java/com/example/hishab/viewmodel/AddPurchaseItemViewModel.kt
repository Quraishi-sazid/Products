package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.ValidationModelWithMessage
import com.example.hishab.models.entities.*
import com.example.hishab.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddPurchaseItemViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    var needToRestoreBackUp: Boolean =true

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
    lateinit var categoryProductMappingList:List<CategoryAndProductModel>

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
        if (backUpCategory.getCategoryName() != category.getCategoryName()) {
            if (backUpCategory.categoryId == category.categoryId) {
                category.categoryId = 0L
            }
        }
        if (backUpProduct.getProductName() != product.getProductName()) {
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

    fun isValidInput(): ValidationModelWithMessage {
        var isInputOkay = !(category.getCategoryName().isNullOrBlank() || product.getProductName().isNullOrBlank())
        if(isInputOkay){
            if(isUpdating){
                var categoryProductModel = categoryProductMappingList.find { categoryAndProductModel -> categoryAndProductModel.getProductName() == product.getProductName() }
                var isNewProduct = categoryProductModel == null
                if(isNewProduct)
                    return ValidationModelWithMessage(true,"")
                if(categoryProductModel!!.getCategoryName() != category.getCategoryName()){
                    return ValidationModelWithMessage(false,"this product belongs to ${categoryProductModel.getCategoryName()} category,can't map it with ${category.getCategoryName()} category. you can change the mapping from category Product Mapping Section")
                }else
                    return ValidationModelWithMessage(true,"")
            }else
                return ValidationModelWithMessage(true,"")
        }
        return ValidationModelWithMessage(false,"category or product name can't be empty")
    }

    fun restoreBackUp() {
        category.categoryId = backUpCategory.categoryId
        category.setCategoryName(backUpCategory.getCategoryName())
        product.productId=backUpProduct.productId
        product.setProductName(backUpProduct.getProductName())
        product.categoryId = backUpCategory.categoryId
    }
}