package com.example.pokeapp.domain.usecase.getallabilities

import com.example.pokeapp.data.repository.ability.AbilityRepository
import com.example.pokeapp.data.repository.poketype.PokeTypeRepository
import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.domain.entity.PokeAbilityName
import com.example.pokeapp.domain.entity.PokeType
import javax.inject.Inject

class GetAllAbilitiesUseCaseImpl @Inject constructor(
    private val pokeAbilityRepository: AbilityRepository
) : GetAllAbilitiesUseCase {

    override suspend fun execute(): Result<List<PokeAbilityName>> =
        try {
            Result.success(
                pokeAbilityRepository.getAllAbilities()
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
}