package com.shoppinglist.repositories

import com.shoppinglist.models.Category
import com.shoppinglist.models.Product
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {

    fun getCategories(): Flow<MutableList<Category>>

    suspend fun insertCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    suspend fun getProducts(categoryId: Int): Flow<MutableList<Product>>

    suspend fun insertProduct(product: Product)

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(product: Product)
}