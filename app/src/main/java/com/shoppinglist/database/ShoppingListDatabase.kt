package com.shoppinglist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shoppinglist.models.Category
import com.shoppinglist.models.Product

@Database(entities = [Category::class, Product::class], version = 1, exportSchema = false)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
}