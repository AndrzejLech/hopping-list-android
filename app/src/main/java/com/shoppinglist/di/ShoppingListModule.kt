package com.shoppinglist.di

import android.content.Context
import androidx.room.Room
import com.shoppinglist.database.ShoppingListDatabase
import com.shoppinglist.repositories.ShoppingListDataSource
import com.shoppinglist.repositories.category.CategoryDataSource
import com.shoppinglist.repositories.category.CategoryRepository
import com.shoppinglist.repositories.ShoppingListRepository
import com.shoppinglist.repositories.product.ProductDataSource
import com.shoppinglist.repositories.product.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ShoppingListModule {

    @Singleton
    @Provides
    fun provideCategoryRepository(shoppingListDatabase: ShoppingListDatabase): CategoryRepository {
        return CategoryDataSource(shoppingListDatabase.categoryDao())
    }

    @Singleton
    @Provides
    fun provideProductRepository(shoppingListDatabase: ShoppingListDatabase): ProductRepository {
        return ProductDataSource(shoppingListDatabase.productDao())
    }


    @Singleton
    @Provides
    fun provideShoppingListRepository(
        categoryRepository: CategoryRepository,
        productRepository: ProductRepository
        ): ShoppingListRepository {
        return ShoppingListDataSource(categoryRepository, productRepository)
    }

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ShoppingListDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ShoppingListDatabase::class.java,
            "shopping_list.db"
        ).build()
    }
}