package com.shoppinglist.repositories.category

import androidx.annotation.WorkerThread
import com.shoppinglist.database.CategoryDao
import com.shoppinglist.models.Category

class CategoryDataSource(private val categoryDao: CategoryDao): CategoryRepository {

    override val categories = categoryDao.get()

    @WorkerThread
    override suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    @WorkerThread
    override suspend fun delete(category: Category) {
        categoryDao.delete(category)
    }

    @WorkerThread
    override suspend fun update(category: Category) {
        categoryDao.update(category)
    }
}