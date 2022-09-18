package com.example.hishab.models.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.hishab.BR
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

@Entity(tableName= "category")
class Category @Inject constructor () :BaseObservable(),Parcelable {
    @NonNull
    @PrimaryKey(autoGenerate = true) @androidx.room.ColumnInfo(name="category_id") var  categoryId:Long=0
    @NonNull
    @ColumnInfo(name="category_name") private var categoryName:String=""
    @ColumnInfo(name="remote_id") var remoteId:Long=-1
    @Bindable
    fun getCategoryName():String
    {
        return categoryName!!
    }
    fun setCategoryName(value:String)
    {
        categoryName=value
        notifyPropertyChanged(BR.categoryName)
    }


    constructor(categoryName: String):this()
    {
        this.categoryName=categoryName
    }

    constructor(parcel: Parcel) : this() {
        categoryId = parcel.readLong()
        categoryName = parcel.readString()!!
    }
    constructor(categoryId:Long,categoryName:String):this()
    {
        this.categoryId=categoryId
        this.setCategoryName(categoryName)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(categoryId!!)
        parcel.writeString(categoryName)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return getCategoryName()!!
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
    fun deepCopy():Category
    {
        var category=Category()
        category.categoryId=categoryId
        category.setCategoryName(""+categoryName)
        return category
    }

}