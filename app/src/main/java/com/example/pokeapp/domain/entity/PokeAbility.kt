package com.example.pokeapp.domain.entity

import com.example.pokeapp.base.EMPTY

data class PokeAbility(
    val name: String,
    val description: String = String.EMPTY
)

data class PokeAbilityName(
    val value: String
)