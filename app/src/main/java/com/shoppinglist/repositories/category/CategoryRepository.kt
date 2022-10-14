package com.shoppinglist.repositories.category

import com.shoppinglist.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    val categories: Flow<MutableList<Category>>

    suspend fun insert(category: Category)

    suspend fun delete(category: Category)

    suspend fun update(category: Category)
}