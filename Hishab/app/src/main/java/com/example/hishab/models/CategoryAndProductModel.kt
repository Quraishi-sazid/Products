package com.example.hishab.models

import android.os.Parcel
import android.os.Parcelable

data class CategoryAndProductModel(
    private val productId:Long,
    private val categoryId:Long,
    private val categoryName:String?,
    private val productName:String?
    ):Parcelable{

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
    fun getCategoryName():String?{
        return categoryName
    }

    fun getProductName():String?{
        return productName
    }
    fun getCategoryId():Long?{
        return categoryId
    }
    fun getProductId():Long?{
        return productId
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
