package com.example.hishab.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.hishab.BR
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.ShoppingItem

class AddItemProxy(public var proxyId:Int,public val category: Category,public val shoppingItem: ShoppingItem,public val purchaseItem: PurchaseItem): BaseObservable() {
 /* private var categoryName=""
  private var itemName=""
  private var description=""
  private var cost=0
  public var Id=0;

    @Bindable
    fun getCategoryName():String
    {
        return categoryName
    }

    @Bindable
    fun getItemName():String
    {
        return itemName
    }

    @Bindable
    fun getDescription():String
    {
        return description
    }
    @Bindable
    fun getCost():Int
    {
        return cost
    }
    fun setCost(c:Int)
    {
        cost=c
        notifyPropertyChanged(BR.cost)
    }
    fun setItemName(c:String)
    {
        itemName=c
        notifyPropertyChanged(BR.itemName)
    }
    fun setCategoryName(c:String)
    {
        categoryName=c
        notifyPropertyChanged(BR.categoryName)
    }
    fun setDescription(c:String)
    {
        description=c
        notifyPropertyChanged(BR.description)
    }*/
/* var proxyId:Int=0*/
/* lateinit var category: Category*/
/* lateinit var shoppingItem: ShoppingItem*/
/* lateinit var purchaseItem: PurchaseItem*/

}