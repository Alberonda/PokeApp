package com.example.pokeapp.data.source.network.entity

data class NetworkAllAbilitiesResponse(
    val results: List<ApiPokeAbility>
) {
    fun toDomainEntity() =
        results.map {
            it.toDomainEntity()
        }
}