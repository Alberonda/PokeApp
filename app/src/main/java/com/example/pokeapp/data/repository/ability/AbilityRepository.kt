package com.example.pokeapp.data.repository.ability

import com.example.pokeapp.domain.entity.PokeAbility

interface AbilityRepository {
    suspend fun getAllAbilities(): List<PokeAbility>
    suspend fun getAbilityDetails(abilityName: String)
}