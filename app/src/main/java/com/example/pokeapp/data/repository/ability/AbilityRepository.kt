package com.example.pokeapp.data.repository.ability

import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.domain.entity.PokeAbilityName

interface AbilityRepository {
    suspend fun getAllAbilities(): List<PokeAbilityName>
    suspend fun getAbilityDetails(abilityName: String): PokeAbility
}