package com.example.pokeapp.data.repository.poketype

import com.example.pokeapp.base.Constants.TYPES_NAMES_SEPARATOR
import com.example.pokeapp.base.mergeByKeys
import com.example.pokeapp.data.source.network.PokeTypeNetworkDataSource
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class PokeTypeRepositoryImpl @Inject constructor(
    private val pokeTypeNetworkDataSource: PokeTypeNetworkDataSource
): PokeTypeRepository {

    override suspend fun getTypesDetails(
        typeNames: List<String>
    ): PokeTypeDetails {
        coroutineScope {
            val deferredResults = typeNames.map {
                async { pokeTypeNetworkDataSource.getTypeData(it) }
            }
            awaitAll(*deferredResults.toTypedArray())
        }.run {
            val pokeTypeDetails = this.map {
                PokeTypeDetails.fromDataEntity(it)
            }

            return PokeTypeDetails(
                pokeTypeDetails.joinToString(
                    TYPES_NAMES_SEPARATOR
                ) { it.name },
                pokeTypeDetails.flatMap {
                    it.attackerTypesRelation
                }.mergeByKeys { a: Double, b: Double -> a * b },
                pokeTypeDetails.flatMap {
                    it.defenderTypesRelation
                }.mergeByKeys { a: Double, b: Double -> a * b }
            )
        }
    }

    override suspend fun getAllTypes(): List<PokeType> =
        pokeTypeNetworkDataSource.getAllTypes().results.map {
            PokeType.fromDataEntity(it)
        }
}