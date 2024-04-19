package com.example.pokeapp.data.repository.poketype

import com.example.pokeapp.data.source.network.entity.ApiPokeType
import com.example.pokeapp.data.source.network.PokeTypeNetworkDataSource
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import javax.inject.Inject

class PokeTypeRepositoryImpl @Inject constructor(
    private val pokeTypeNetworkDataSource: PokeTypeNetworkDataSource
): PokeTypeRepository {

    override suspend fun getTypeDetails(
        typeName: String
    ): PokeTypeDetails =
        PokeTypeDetails.fromDataEntity(
            pokeTypeNetworkDataSource.getTypeData(typeName)
        )

    override suspend fun getAllTypes(): List<PokeType> =
        pokeTypeNetworkDataSource.getAllTypes().results.map {
            PokeType.fromDataEntity(it)
        }

}