package com.example.pokeapp.data.source.network.entity

import com.example.pokeapp.domain.entity.PokeAbilityName

data class ApiPokeAbility(
    val name: String
) {
    fun toDomainEntity() =
        PokeAbilityName(name)
}
