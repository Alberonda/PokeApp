package com.example.pokeapp.presentation.abilitieslanding

import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.domain.entity.PokeAbilityName

data class AbilitiesLandingScreenUiData (
    val isSearching: Boolean = false,
    val searchText: String = String.EMPTY,
    val suggestedAbilities: List<PokeAbilityName> = emptyList(),
    val selectedAbilityData: PokeAbility? = null
)