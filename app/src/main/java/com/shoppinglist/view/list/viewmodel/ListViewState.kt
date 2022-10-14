package com.shoppinglist.view.list.viewmodel

sealed class ListViewState {
    object EmptyList: ListViewState()

    object Items: ListViewState()

    object FailedToUpdate: ListViewState()

    data class UnexpectedError(val cause: Throwable) : ListViewState()
}