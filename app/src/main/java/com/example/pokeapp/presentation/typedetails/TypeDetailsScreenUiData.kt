package com.example.pokeapp.presentation.typedetails

import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData

data class TypeDetailsScreenUiData (
    val searchedTypes: List<PokeTypeUiData> = emptyList(),
    val veryEffectiveRelations: List<PokeTypeUiData> = emptyList(),
    val effectiveRelations: List<PokeTypeUiData> = emptyList(),
    val notEffectiveRelations: List<PokeTypeUiData> = emptyList(),
    val veryNotEffectiveRelations: List<PokeTypeUiData> = emptyList(),
    val unaffectedRelations: List<PokeTypeUiData> = emptyList(),
)