package com.shoppinglist.view.createSection

sealed class CreateSectionViewState{
    object EmptyList: CreateSectionViewState()

    object NotUnique: CreateSectionViewState()

    data class UnexpectedError(val cause: Throwable) : CreateSectionViewState()

    object IsBlank: CreateSectionViewState()

    object ToShortName: CreateSectionViewState()

    object ResultSuccess: CreateSectionViewState()
}
