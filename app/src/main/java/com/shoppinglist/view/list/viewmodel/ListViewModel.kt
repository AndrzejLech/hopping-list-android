package com.shoppinglist.view.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoppinglist.models.Category
import com.shoppinglist.models.Product
import com.shoppinglist.repositories.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    private val _categories: MutableLiveData<Flow<List<Category>>> = MutableLiveData()
    val categories: LiveData<Flow<List<Category>>> = _categories

    private val _products: MutableLiveData<Flow<List<Product>>> = MutableLiveData()
    val products: LiveData<Flow<List<Product>>> = _products

    private val _error: MutableLiveData<ListViewState> = MutableLiveData()
    val error: LiveData<ListViewState> = _error

    fun getCategories() {
        viewModelScope.launch {
            shoppingListRepository.getCategories().collect {
                try {
                    when {
                        it.isEmpty() -> {
                            _error.value = ListViewState.EmptyList
                        }
                        else -> {
                            _categories.value = shoppingListRepository.getCategories()
                            _error.value = ListViewState.Items
                        }
                    }
                } catch (exception: Exception) {
                    if (exception is IOException) {
                        _error.value = ListViewState.UnexpectedError(exception)
                    }
                }
            }
        }
    }

    fun getProducts(categoryId: Int) {
        viewModelScope.launch {
            try {
                shoppingListRepository.getProducts(categoryId).collect {
                    _products.value = shoppingListRepository.getProducts(categoryId)
                }
            } catch (exception: Exception) {
                _error.value = ListViewState.UnexpectedError(exception)
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {
                shoppingListRepository.updateProduct(product)
            } catch (exception: Exception) {
                _error.value = ListViewState.FailedToUpdate
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            shoppingListRepository.deleteProduct(product)
        }
    }
}