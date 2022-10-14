package com.shoppinglist.view.deleteDialog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoppinglist.models.Product
import com.shoppinglist.repositories.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteDialogViewModel @Inject constructor(
    val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    fun delete(product: Product) {
        viewModelScope.launch {
            shoppingListRepository.deleteProduct(product)
        }
    }
}