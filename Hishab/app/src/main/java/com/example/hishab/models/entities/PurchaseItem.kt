package com.example.hishab.models.entities

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*
import javax.inject.Inject


@Entity(tableName = "purchase_table")
class PurchaseItem @Inject constructor (): BaseObservable() {
    private @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "purchase_id") var purchaseId: Int = 0
    private @ColumnInfo(name = "cost") var cost: Int=0
    @Bindable
    public fun getPurchaseId():Int
    {
        return purchaseId
    }
    public fun setPurchaseId(value: Int)
    {
        purchaseId=value
        notifyPropertyChanged(BR.purchaseId)
    }
    @Bindable
    public fun getCost():Int
    {
        return cost
    }
    public fun setCost(value: Int)
    {
        cost=value
        notifyPropertyChanged(BR.cost)
    }
    private @ColumnInfo(name = "description") var description=""
    public @ColumnInfo(name = "buying_id") var buyingId=0
    @Bindable
    public fun getDescription():String
    {
        return description
    }
    public fun setDescription(value: String)
    {
        description=value
        notifyPropertyChanged(BR.description)
    }

    //private @ColumnInfo(name = "shopping_id") var s=""


   // public @ColumnInfo(name = "createdAt") var createdAt: Date? = null
/*   public @ColumnInfo(name = "day") var day: Int = 0
   public @ColumnInfo(name = "month") var month: Int = 0
   public @ColumnInfo(name = "year") var year: Int = 0*/

    @ColumnInfo(name = "product_id") var productId:Int=0

}

