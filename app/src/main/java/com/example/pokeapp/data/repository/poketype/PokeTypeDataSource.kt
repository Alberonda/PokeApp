package com.example.pokeapp.data.repository.poketype

import com.example.pokeapp.base.Constants.TYPES_NAMES_SEPARATOR
import com.example.pokeapp.base.getDifferenceInMinutes
import com.example.pokeapp.base.mergeByKeys
import com.example.pokeapp.data.source.local.PokeAppDatabase.Companion.CACHE_TIME_MINUTES
import com.example.pokeapp.data.source.local.PokeTypeLocalDataSource
import com.example.pokeapp.data.source.network.PokeTypeNetworkDataSource
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import com.example.pokeapp.data.source.local.entity.PokeType as LocalPokeType
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date
import javax.inject.Inject

class PokeTypeDataSource @Inject constructor(
    private val pokeTypeNetworkDataSource: PokeTypeNetworkDataSource,
    private val pokeTypeLocalDataSource: PokeTypeLocalDataSource
) : PokeTypeRepository {

    override suspend fun getTypesDetails(
        typeNames: List<String>
    ): PokeTypeDetails {
        coroutineScope {
            val deferredResults = typeNames.map {
                async {
                    getTypeData(it)
                }
            }
            awaitAll(*deferredResults.toTypedArray())
        }.run {
            return PokeTypeDetails(
                this.joinToString(
                    TYPES_NAMES_SEPARATOR
                ) { it.name },
                this.flatMap {
                    it.attackerTypesRelation
                }.mergeByKeys { a: Double, b: Double -> a * b },
                this.flatMap {
                    it.defenderTypesRelation
                }.mergeByKeys { a: Double, b: Double -> a * b }
            )
        }
    }

    private suspend fun getTypeData(typeName: String): PokeTypeDetails {
        val localData = pokeTypeLocalDataSource.getPokeTypeByName(typeName)
            .firstOrNull()

        return if (
            localData == null ||
            localData.insertionDate.getDifferenceInMinutes(Date()) >= CACHE_TIME_MINUTES
        ) {
            getRemoteTypeData(typeName)
        } else {
            localData.toDomainEntity()
        }
    }

    private suspend fun getRemoteTypeData(typeName: String): PokeTypeDetails {
        val networkData = pokeTypeNetworkDataSource.getTypeData(typeName)

        pokeTypeLocalDataSource.insertPokeType(LocalPokeType.fromNetworkEntity(networkData))
        return networkData.toDomainEntity()
    }

    override suspend fun getAllTypes(): List<PokeType> =
        pokeTypeNetworkDataSource.getAllTypes().results.map {
            PokeType.fromDataEntity(it)
        }
}