package com.example.pokeapp.data.source.network.entity

import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import com.google.gson.annotations.SerializedName

data class TypeDetailsResponse(
    val name: String,
    @SerializedName("damage_relations") val damageRelations: ApiTypeRelations
) {
    fun toDomainEntity() =
        PokeTypeDetails(
            name = name,
            attackerTypesRelation = damageRelations.doubleDamageTo.map {
                Pair(PokeType(it.name), PokeTypeDetails.DOUBLE_DAMAGE_FACTOR)
            } + damageRelations.halfDamageTo.map {
                Pair(PokeType(it.name), PokeTypeDetails.HALF_DAMAGE_FACTOR)
            } + damageRelations.noDamageTo.map {
                Pair(PokeType(it.name), PokeTypeDetails.NO_DAMAGE_FACTOR)
            },
            defenderTypesRelation = damageRelations.doubleDamageFrom.map {
                Pair(PokeType(it.name), PokeTypeDetails.DOUBLE_DAMAGE_FACTOR)
            } + damageRelations.halfDamageFrom.map {
                Pair(PokeType(it.name), PokeTypeDetails.HALF_DAMAGE_FACTOR)
            } + damageRelations.noDamageFrom.map {
                Pair(PokeType(it.name), PokeTypeDetails.NO_DAMAGE_FACTOR)
            },
        )
}

data class ApiTypeRelations(
    @SerializedName("double_damage_from") val doubleDamageFrom: List<ApiPokeType>,
    @SerializedName("double_damage_to") val doubleDamageTo: List<ApiPokeType>,
    @SerializedName("half_damage_from") val halfDamageFrom: List<ApiPokeType>,
    @SerializedName("half_damage_to") val halfDamageTo: List<ApiPokeType>,
    @SerializedName("no_damage_from") val noDamageFrom: List<ApiPokeType>,
    @SerializedName("no_damage_to") val noDamageTo: List<ApiPokeType>
)
