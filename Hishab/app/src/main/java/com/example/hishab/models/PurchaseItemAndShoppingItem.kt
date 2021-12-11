package com.example.hishab.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.Product


data class PurchaseItemAndShoppingItem(@Embedded val product: Product,
                                       @Relation(
                                           parentColumn = "shopping_id",
                                           entityColumn = "shopping_id"
                                   )
                                   val purchaseItem: List<PurchaseItem>
                                   )
