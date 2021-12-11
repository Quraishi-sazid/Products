package com.example.hishab.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.Product



data class CategoryAndShoppingItem(@Embedded val category: Category,
                                   @Relation(
                                           parentColumn = "category_id",
                                           entityColumn = "category_id"
                                   )
                                   val products: List<Product>
                                   )

