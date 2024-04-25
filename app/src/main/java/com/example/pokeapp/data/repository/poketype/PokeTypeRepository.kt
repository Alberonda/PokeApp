package com.example.pokeapp.data.repository.poketype

import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails

interface PokeTypeRepository {
    suspend fun getAllTypes(): List<PokeType>
    suspend fun getTypesDetails(typeNames: List<String>): PokeTypeDetails
}