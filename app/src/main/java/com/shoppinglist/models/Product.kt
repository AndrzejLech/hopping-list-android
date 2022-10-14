package com.shoppinglist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_table",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id"], unique = true),
    ]
)
class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "category_id")
    val categoryId: Int = 0,
    val order: Int = 0,
    val name: String = "",
    var inCart: Boolean = false
)