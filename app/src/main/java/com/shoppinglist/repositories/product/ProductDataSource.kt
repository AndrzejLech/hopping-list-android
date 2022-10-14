package com.shoppinglist.repositories.product

import androidx.annotation.WorkerThread
import com.shoppinglist.database.ProductDao
import com.shoppinglist.models.Product
import kotlinx.coroutines.flow.Flow

class ProductDataSource(private val productDao: ProductDao) : ProductRepository {

    @WorkerThread
    override suspend fun productsByCategory(categoryId: Int): Flow<MutableList<Product>> {
        return productDao.getAllByCategory(categoryId)
    }

    @WorkerThread
    override suspend fun insert(product: Product) {
        productDao.insert(product)
    }

    @WorkerThread
    override suspend fun update(product: Product) {
        productDao.update(product)
    }

    @WorkerThread
    override suspend fun delete(product: Product) {
        productDao.delete(product)
    }
}