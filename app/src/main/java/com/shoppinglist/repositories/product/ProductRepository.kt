package com.shoppinglist.repositories.product

import com.shoppinglist.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun productsByCategory(categoryId: Int): Flow<MutableList<Product>>

    suspend fun insert(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(product: Product)
}