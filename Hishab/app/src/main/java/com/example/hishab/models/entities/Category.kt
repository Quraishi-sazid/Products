package com.example.hishab.models.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hishab.BR
import com.example.hishab.utils.Util

@Entity(tableName= "category")
data class Category(@PrimaryKey(autoGenerate = true) @androidx.room.ColumnInfo(name="category_id") var  categoryId:Int=0) :BaseObservable() {
    @ColumnInfo(name="category_name") private var CategoryName:String=""
    @Bindable
    fun getCategoryName():String
    {
        return CategoryName
    }
    fun setCategoryName(value:String)
    {
        Util.getMonthFromDateTime(35);
        CategoryName=value
        notifyPropertyChanged(BR.categoryName)
    }

}