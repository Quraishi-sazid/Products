package com.example.hishab.models.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.*
import com.example.hishab.BR
import javax.inject.Inject

@Entity(tableName = "product_table")
class Product @Inject constructor() :
    BaseObservable() {
    constructor(productName: String, categoryId: Long,productId: Long=0):this()
    {
        this.productId=productId
        this.productName=productName
        this.categoryId=categoryId
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    private var _productId: Long = 0
    var payloadId:Long = -1

    @ColumnInfo(name = "product_name")
    private var productName: String = ""

    @Bindable
    fun getProductName(): String {
        return productName
    }

    fun setProductName(value: String) {
        productName = value
        notifyPropertyChanged(BR.itemName)

    }

    @ColumnInfo(name = "category_id")
    var categoryId: Long = 0
    var productId: Long
        get() = _productId
        set(value) {
            _productId = value
        }

    fun deepCopy(): Product {
        var shoppingItem = Product()
        shoppingItem.categoryId = categoryId
        shoppingItem.productId = productId
        shoppingItem.productName = "" + productName;
        return shoppingItem
    }


}
