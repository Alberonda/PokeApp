package com.example.pokeapp.presentation.abilitysearch.entity

import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.domain.entity.PokeAbility

data class AbilitySearchScreenUiData (
    val isSearching: Boolean = false,
    val searchText: String = String.EMPTY,
    val suggestedAbilities: List<PokeAbility> = emptyList()
)