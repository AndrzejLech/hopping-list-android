package com.shoppinglist.view.createSection.viewmodel

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoppinglist.models.Category
import com.shoppinglist.models.Product
import com.shoppinglist.repositories.ShoppingListRepository
import com.shoppinglist.view.createSection.CreateSectionViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSectionViewModel @Inject constructor(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    private val TAG = "CreateSectionViewModel"

    private val _categories: MutableLiveData<Flow<List<Category>>> = MutableLiveData()
    val categories: LiveData<Flow<List<Category>>> = _categories

    private val _products: MutableLiveData<Flow<List<Product>>> = MutableLiveData()
    val products: LiveData<Flow<List<Product>>> = _products

    private val _error: MutableLiveData<CreateSectionViewState> = MutableLiveData()
    val error: LiveData<CreateSectionViewState> = _error

    fun getCategories() {
        viewModelScope.launch {
            try {
                shoppingListRepository.getCategories().collect { categories ->
                    if (categories.isEmpty()) {
                        _error.value = CreateSectionViewState.EmptyList
                    }
                    _categories.value = shoppingListRepository.getCategories()
                }
            } catch (exception: Exception) {
                _error.value = CreateSectionViewState.UnexpectedError(exception)
            }
        }
    }

    fun getProducts(categoryId: Int) {
        viewModelScope.launch {
            try {
                shoppingListRepository.getProducts(categoryId).collect { products ->
                    if (products.isEmpty()) {
                        _error.value = CreateSectionViewState.EmptyList
                    }
                    _products.value = shoppingListRepository.getProducts(categoryId)
                }
            } catch (exception: Exception) {
                _error.value = CreateSectionViewState.UnexpectedError(exception)
            }
        }
    }

    fun insertCategory(categoryName: String, categories: List<Category>) {
        kotlin.runCatching {
            when {
                categoryName.isBlank() -> CreateSectionViewState.IsBlank
                categoryName.length <= 3 -> CreateSectionViewState.ToShortName
                else -> {
                    viewModelScope.launch {
                        shoppingListRepository.insertCategory(
                            Category(name = categoryName, order = categories.size + 1)
                        )
                    }
                }
            }
        }.onFailure { throwable ->
            if (throwable is SQLiteConstraintException) {
                _error.value = CreateSectionViewState.NotUnique
            } else {
                _error.value = CreateSectionViewState.UnexpectedError(throwable)
            }
            Log.e(TAG, throwable.toString())
        }.onSuccess {
            _error.value = CreateSectionViewState.ResultSuccess
        }
    }

    fun insertProduct(productName: String, category: Category, products: List<Product>) {
        kotlin.runCatching {
            when {
                productName.isBlank() -> _error.value = CreateSectionViewState.IsBlank
                productName.length <= 3 -> _error.value = CreateSectionViewState.ToShortName
                else -> {
                    viewModelScope.launch {
                        shoppingListRepository.insertProduct(
                            Product(
                                name = productName,
                                categoryId = category.id,
                                order = products.size + 1
                            )
                        )
                    }
                    _error.value = CreateSectionViewState.ResultSuccess
                }
            }
        }
    }
}