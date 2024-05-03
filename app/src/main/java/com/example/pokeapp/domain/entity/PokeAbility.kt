package com.example.pokeapp.domain.entity

import com.example.pokeapp.base.EMPTY

data class PokeAbility(
    val name: String,
    val description: String = String.EMPTY,
    val otherNames: List<OtherLanguageName> = emptyList()
)

data class PokeAbilityName(
    val value: String
)

data class OtherLanguageName(
    val value: String,
    val language: String
)