package com.example.pokeapp.domain.usecase.getalltypes

import com.example.pokeapp.data.repository.poketype.PokeTypeRepository
import com.example.pokeapp.domain.entity.PokeType
import javax.inject.Inject

class GetAllTypesUseCaseImpl @Inject constructor(
    private val pokeTypeRepository: PokeTypeRepository
) : GetAllTypesUseCase {

    override suspend fun execute(): Result<List<PokeType>> =
        try {
            Result.success(
                pokeTypeRepository.getAllTypes().take(NUMBER_OF_EXISTING_TYPES)
            )
        } catch (e: Exception) {
            Result.failure(e)
        }

    companion object {
        private const val NUMBER_OF_EXISTING_TYPES = 18
    }
}


