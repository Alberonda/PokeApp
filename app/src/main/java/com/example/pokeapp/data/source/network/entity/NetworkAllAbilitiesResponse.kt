package com.example.pokeapp.data.source.network.entity

import com.example.pokeapp.domain.entity.PokeAbility

data class NetworkAllAbilitiesResponse(
    val results: List<ApiPokeAbility>
) {
    fun toDomainEntity() =
        results.map {
            it.toDomainEntity()
        }
}