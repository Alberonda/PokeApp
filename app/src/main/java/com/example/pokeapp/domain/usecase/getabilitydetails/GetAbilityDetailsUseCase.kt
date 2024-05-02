package com.example.pokeapp.domain.usecase.getallabilities

import com.example.pokeapp.domain.entity.PokeAbility

interface GetAbilityDetailsUseCase {
    suspend fun execute(abilityName: String): Result<PokeAbility>
}

