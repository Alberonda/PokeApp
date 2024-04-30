package com.example.pokeapp.presentation.abilitysearch.entity

import com.example.pokeapp.base.EMPTY

data class AbilitySearchScreenUiData (
    val isSearching: Boolean = false,
    val searchText: String = String.EMPTY,
    val abilitiesList: List<String> = emptyList()
)