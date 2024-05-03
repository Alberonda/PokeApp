package com.example.pokeapp.domain.usecase.getallabilities

import com.example.pokeapp.domain.entity.PokeAbilityName

interface GetAllAbilitiesUseCase {
    suspend fun execute(): Result<List<PokeAbilityName>>
}

