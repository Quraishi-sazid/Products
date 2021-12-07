package com.example.hishab.models.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.*
import com.example.hishab.BR
import javax.inject.Inject

@Entity(tableName = "product_table")
class ShoppingItem @Inject constructor():
    BaseObservable() {
    private @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "product_id") var _productId:Long=0
    private @ColumnInfo(name = "product_name")  var productName:String=""

    @Bindable
    public fun getProductName():String
    {
        return productName
    }

    public fun setProductName(value:String)
    {
        productName=value
        notifyPropertyChanged(BR.itemName)

    }
    @ColumnInfo(name="category_id") var  CategoryId:Long=0
    var productId:Long
        get() = _productId
        set(value)
        {
            _productId=value
        }

    fun copyOf():ShoppingItem
    {
        var shoppingItem=ShoppingItem()
        shoppingItem.CategoryId=CategoryId
        shoppingItem.productId=productId
        shoppingItem.productName=""+productName;
        return shoppingItem
    }


}
