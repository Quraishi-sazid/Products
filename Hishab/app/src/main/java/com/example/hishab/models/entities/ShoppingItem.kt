package com.example.hishab.models.entities

import android.app.Application
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.*
import com.example.hishab.BR
import com.example.hishab.repository.ShoppingRepository
import javax.inject.Inject

@Entity(tableName = "shopping_table")
class ShoppingItem @Inject constructor():
    BaseObservable() {
    private @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "shopping_id") var _itemId:Int=0
    private @ColumnInfo(name = "item_name")  var ItemName:String=""

    @Bindable
    public fun getItemName():String
    {
        return ItemName
    }

    public fun setItemName(value:String)
    {
        ItemName=value
        notifyPropertyChanged(BR.itemName)

    }
    @ColumnInfo(name="category_id") var  CategoryId:Int=0
    var itemId:Int
        get() = _itemId
        set(value)
        {
            _itemId=value
        }



}
