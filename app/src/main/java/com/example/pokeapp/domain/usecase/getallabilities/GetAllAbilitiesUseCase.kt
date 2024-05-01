package com.example.pokeapp.domain.usecase.getallabilities

import com.example.pokeapp.domain.entity.PokeAbility

interface GetAllAbilitiesUseCase {
    suspend fun execute(): Result<List<PokeAbility>>
}

