package com.example.hishab.models.entities

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class CategoryAndProductModel(
    private val productId:Int,
    private val categoryId:Int,
    private val categoryName:String?,
    private val productName:String?
    ):Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
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
    fun getCategoryId():Int?{
        return categoryId
    }
    fun getProductId():Int?{
        return productId
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(productId)
        parcel.writeInt(categoryId)
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
