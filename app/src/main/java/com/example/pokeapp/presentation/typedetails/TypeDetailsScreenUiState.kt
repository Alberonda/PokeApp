package com.example.pokeapp.presentation.typedetails

import com.example.pokeapp.presentation.typedetails.entity.TypeDetailsScreenUiData

data class TypeDetailsScreenUiState(
    val typesDetails: TypeDetailsScreenUiData? = null,
    val isLoading: Boolean = false,
    val throwError: Boolean = false
)
