package com.example.pokeapp.presentation.typeslanding

import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData

data class TypesLandingScreenUiState(
    val allTypes: List<PokeTypeUiData> = emptyList(),
    val isLoading: Boolean = false,
    val throwError: Boolean = false
)
