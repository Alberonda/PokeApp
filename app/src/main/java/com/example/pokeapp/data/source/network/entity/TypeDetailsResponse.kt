package com.example.pokeapp.data.source.network.entity

import com.google.gson.annotations.SerializedName

data class TypeDetailsResponse(
    val name: String,
    @SerializedName("damage_relations") val damageRelations: ApiTypeRelations
)

data class ApiTypeRelations(
    @SerializedName("double_damage_from") val doubleDamageFrom: List<ApiPokeType>,
    @SerializedName("double_damage_to") val doubleDamageTo: List<ApiPokeType>,
    @SerializedName("half_damage_from") val halfDamageFrom: List<ApiPokeType>,
    @SerializedName("half_damage_to") val halfDamageTo: List<ApiPokeType>,
    @SerializedName("no_damage_from") val noDamageFrom: List<ApiPokeType>,
    @SerializedName("no_damage_to") val noDamageTo: List<ApiPokeType>
)
