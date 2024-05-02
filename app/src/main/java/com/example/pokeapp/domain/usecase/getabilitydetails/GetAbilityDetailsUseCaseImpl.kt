package com.example.pokeapp.domain.usecase.getallabilities

import com.example.pokeapp.data.repository.ability.AbilityRepository
import com.example.pokeapp.domain.entity.PokeAbility
import javax.inject.Inject

class GetAbilityDetailsUseCaseImpl @Inject constructor(
    private val pokeAbilityRepository: AbilityRepository
) : GetAbilityDetailsUseCase {

    override suspend fun execute(abilityName: String): Result<PokeAbility> =
        try {
            Result.success(
                pokeAbilityRepository.getAbilityDetails(abilityName)
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
}