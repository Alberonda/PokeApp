package com.example.pokeapp.domain.usecase.gettypedetails

import com.example.pokeapp.domain.entity.PokeTypeDetails

interface GetTypeDetailsUseCase {
    suspend fun execute(typeName: String): Result<PokeTypeDetails>
}
