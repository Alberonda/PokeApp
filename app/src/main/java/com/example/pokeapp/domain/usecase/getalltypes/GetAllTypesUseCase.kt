package com.example.pokeapp.domain.usecase.getalltypes

import com.example.pokeapp.domain.entity.PokeType

interface GetAllTypesUseCase {
    suspend fun execute(): Result<List<PokeType>>
}

