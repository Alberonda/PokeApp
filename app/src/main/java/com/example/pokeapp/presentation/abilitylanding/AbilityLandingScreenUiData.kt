package com.example.pokeapp.presentation.abilitylanding

import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.domain.entity.PokeAbility

data class AbilityLandingScreenUiData (
    val isSearching: Boolean = false,
    val searchText: String = String.EMPTY,
    val suggestedAbilities: List<PokeAbility> = emptyList(),
    val selectedAbilityData: PokeAbility? = null
)