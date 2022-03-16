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
import java.io.Serializable


@Entity(tableName= "tbl_category_budget")
class Budget():BaseObservable(), Serializable, Parcelable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="budget_id")
    private var budgetId : Long=0

    @NonNull
    @Bindable
    @ColumnInfo(name="category_id")
    var categoryId:Long=0

    @NonNull
    @Bindable
    @ColumnInfo(name="budget")
    var budget: Int=0

    @NonNull
    @ColumnInfo(name="month")
    var month:Int=0

    @NonNull
    @ColumnInfo(name="year")
    var year:Int=0

    @Bindable
    @Ignore
    var categoryName:String?=null

    @Bindable
    @Ignore
    var spent:Int=0

    fun getBudgetId():Long
    {
        return budgetId
    }
    fun setBudgetId(value:Long)
    {
        budgetId=value
    }

    @Bindable
    fun getBalanceLeft():Int
    {
        return budget-spent
    }
    constructor(categoryId:Long,budget:Int,month:Int,year:Int):this()
    {
        this.categoryId=categoryId
        this.budget=budget
        this.month=month
        this.year=year
    }

    constructor(parcel: Parcel) : this() {
        budgetId = parcel.readLong()
        categoryId = parcel.readLong()
        budget = parcel.readInt()
        month = parcel.readInt()
        year = parcel.readInt()
        categoryName = parcel.readString()
        spent = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(budgetId)
        parcel.writeLong(categoryId)
        parcel.writeInt(budget)
        parcel.writeInt(month)
        parcel.writeInt(year)
        parcel.writeString(categoryName)
        parcel.writeInt(spent)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Budget> {
        override fun createFromParcel(parcel: Parcel): Budget {
            return Budget(parcel)
        }

        override fun newArray(size: Int): Array<Budget?> {
            return arrayOfNulls(size)
        }
    }

}