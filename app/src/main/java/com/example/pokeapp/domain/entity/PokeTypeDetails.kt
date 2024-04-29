package com.example.pokeapp.domain.entity

import com.example.pokeapp.base.EMPTY

data class PokeTypeDetails(
    val name: String = String.EMPTY,
    val attackerTypesRelation: List<Pair<PokeType, Double>> = emptyList(),
    val defenderTypesRelation: List<Pair<PokeType, Double>> = emptyList()
) {
    companion object {
        const val QUAD_DAMAGE_FACTOR = 4.0
        const val DOUBLE_DAMAGE_FACTOR = 2.0
        const val HALF_DAMAGE_FACTOR = 0.5
        const val QUARTER_DAMAGE_FACTOR = 0.25
        const val NO_DAMAGE_FACTOR = 0.0
    }
}