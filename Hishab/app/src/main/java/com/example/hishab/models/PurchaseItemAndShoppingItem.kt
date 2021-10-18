package com.example.hishab.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.ShoppingItem


data class PurchaseItemAndShoppingItem(@Embedded val shoppingItem: ShoppingItem,
                                       @Relation(
                                           parentColumn = "shopping_id",
                                           entityColumn = "shopping_id"
                                   )
                                   val purchaseItem: List<PurchaseItem>
                                   )
