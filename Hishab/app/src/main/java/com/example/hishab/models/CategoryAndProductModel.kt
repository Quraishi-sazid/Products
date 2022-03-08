package com.example.hishab.models

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

data class CategoryAndProductModel(
    private var productId:Long,
    private var categoryId:Long,
    private var categoryName:String?,
    private var productName:String?
    ):BaseObservable(), Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return productName!!
    }
    @Bindable
    fun getCategoryName():String?{
        return categoryName
    }
    @Bindable
    fun getProductName():String?{
        return productName
    }
    @Bindable
    fun getCategoryId():Long?{
        return categoryId
    }
    @Bindable
    fun getProductId():Long?{
        return productId
    }

    fun setCategoryName(categoryName: String){
        this.categoryName=categoryName
    }
    fun setProductName(categoryName: String){
        this.productName=productName
    }



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(productId)
        parcel.writeLong(categoryId)
        parcel.writeString(categoryName)
        parcel.writeString(productName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryAndProductModel> {
        override fun createFromParcel(parcel: Parcel): CategoryAndProductModel {
            return CategoryAndProductModel(parcel)
        }

        override fun newArray(size: Int): Array<CategoryAndProductModel?> {
            return arrayOfNulls(size)
        }
    }
}
