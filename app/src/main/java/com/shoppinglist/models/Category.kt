package com.shoppinglist.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_table",
    indices = [
        Index(value = ["name"], unique = true),
        Index(value = ["id"], unique = true)
    ]
)
class Category(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var order: Int = 0,
    var name: String = ""
)