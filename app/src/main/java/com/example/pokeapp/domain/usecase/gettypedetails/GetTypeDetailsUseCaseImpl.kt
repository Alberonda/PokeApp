package com.example.pokeapp.domain.usecase.gettypedetails

import com.example.pokeapp.data.repository.poketype.PokeTypeRepository
import com.example.pokeapp.domain.entity.PokeTypeDetails
import javax.inject.Inject

class GetTypeDetailsUseCaseImpl @Inject constructor(
    private val pokeTypeRepository: PokeTypeRepository
): GetTypeDetailsUseCase {

    override suspend fun execute(typeName: String): Result<PokeTypeDetails> =
        try {
            Result.success(
                pokeTypeRepository.getTypeDetails(typeName)
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
}