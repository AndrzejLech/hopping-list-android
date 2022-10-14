package com.shoppinglist.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shoppinglist.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("SELECT * FROM product_table WHERE category_id LIKE :categoryId ORDER BY inCart ASC")
    fun getAllByCategory(categoryId: Int): Flow<MutableList<Product>>
}