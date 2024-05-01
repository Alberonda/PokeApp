package com.example.pokeapp.data.source.network.entity

import com.example.pokeapp.domain.entity.PokeAbility

data class ApiPokeAbility(
    val name: String
) {
    fun toDomainEntity() =
        PokeAbility(name)
}
