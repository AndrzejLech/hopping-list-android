package com.shoppinglist.repositories

import com.shoppinglist.models.Category
import com.shoppinglist.models.Product
import com.shoppinglist.repositories.category.CategoryRepository
import com.shoppinglist.repositories.product.ProductRepository
import kotlinx.coroutines.flow.Flow

class ShoppingListDataSource(
    private val categoryDataSource: CategoryRepository,
    private val productDataSource: ProductRepository
) : ShoppingListRepository {
    override fun getCategories(): Flow<MutableList<Category>> {
        return categoryDataSource.categories
    }

    override suspend fun insertCategory(category: Category) {
        categoryDataSource.insert(category)
    }

    override suspend fun updateCategory(category: Category) {
        categoryDataSource.update(category)
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDataSource.delete(category)
    }

    override suspend fun getProducts(categoryId: Int): Flow<MutableList<Product>> {
        return productDataSource.productsByCategory(categoryId)
    }

    override suspend fun insertProduct(product: Product) {
        productDataSource.insert(product)
    }

    override suspend fun updateProduct(product: Product) {
        productDataSource.update(product)
    }

    override suspend fun deleteProduct(product: Product) {
        productDataSource.delete(product)
    }
}