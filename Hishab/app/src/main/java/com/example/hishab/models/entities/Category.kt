package com.example.hishab.models.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hishab.BR
import com.example.hishab.utils.Util
import javax.inject.Inject

@Entity(tableName= "category")
class Category @Inject constructor () :BaseObservable(),Parcelable {
    @PrimaryKey(autoGenerate = true) @androidx.room.ColumnInfo(name="category_id") var  categoryId:Int=0
    @ColumnInfo(name="category_name") private var CategoryName:String=""
    @Bindable
    fun getCategoryName():String
    {
        return CategoryName
    }
    fun setCategoryName(value:String)
    {
        CategoryName=value
        notifyPropertyChanged(BR.categoryName)
    }


    constructor(parcel: Parcel) : this() {
        categoryId = parcel.readInt()
        CategoryName = parcel.readString()!!
    }
    constructor(categoryId:Int,categoryName:String):this()
    {
        this.categoryId=categoryId
        this.setCategoryName(categoryName)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(categoryId)
        parcel.writeString(CategoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return getCategoryName()
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }

}