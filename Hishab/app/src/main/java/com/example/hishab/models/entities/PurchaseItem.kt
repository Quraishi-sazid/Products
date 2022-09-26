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


@Entity(tableName = "tbl_shopping_item")
class PurchaseItem @Inject constructor() : BaseObservable() {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "purchase_id")
    private var purchaseId: Long = 0

    @ColumnInfo(name = "cost")
    private var cost: Int = 0

    private @ColumnInfo(name = "description")
    var description = ""

    @ColumnInfo(name = "shopping_id")
    var shoppingId = 0L

    @ColumnInfo(name = "product_id")
    var productId: Long = 0

    @Bindable
    public fun getPurchaseId(): Long {
        return purchaseId
    }

    var isSynced = false
    public fun setPurchaseId(value: Long) {
        purchaseId = value
        notifyPropertyChanged(BR.purchaseId)
    }
    var remoteId:Int = -1

    @Bindable
    public fun getCost(): Int {
        return cost
    }

    public fun setCost(value: Int) {
        cost = value
        notifyPropertyChanged(BR.cost)
    }
    @Bindable
    fun getDescription(): String {
        return description
    }

    fun setDescription(value: String) {
        description = value
        notifyPropertyChanged(BR.description)
    }

    constructor(shoppingId:Long,productId:Long,cost:Int) : this() {
        this.shoppingId=shoppingId
        this.productId=productId
        this.cost=cost
    }
}

