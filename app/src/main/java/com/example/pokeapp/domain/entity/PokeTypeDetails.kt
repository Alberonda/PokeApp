package com.example.pokeapp.domain.entity

import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse

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

        fun fromDataEntity(dataEntity: TypeDetailsResponse) =
            dataEntity.run {
                PokeTypeDetails(
                    name = name,
                    attackerTypesRelation = damageRelations.doubleDamageTo.map {
                        Pair(PokeType(it.name), DOUBLE_DAMAGE_FACTOR)
                    } + damageRelations.halfDamageTo.map {
                        Pair(PokeType(it.name), HALF_DAMAGE_FACTOR)
                    } + damageRelations.noDamageTo.map {
                        Pair(PokeType(it.name), NO_DAMAGE_FACTOR)
                    },
                    defenderTypesRelation = damageRelations.doubleDamageFrom.map {
                        Pair(PokeType(it.name), DOUBLE_DAMAGE_FACTOR)
                    } + damageRelations.halfDamageFrom.map {
                        Pair(PokeType(it.name), HALF_DAMAGE_FACTOR)
                    } + damageRelations.noDamageFrom.map {
                        Pair(PokeType(it.name), NO_DAMAGE_FACTOR)
                    },
                )
            }
    }
}