package com.example.pokeapp.data.repository.ability

import com.example.pokeapp.base.getDifferenceInMinutes
import com.example.pokeapp.data.source.local.PokeAbilityLocalDataSource
import com.example.pokeapp.data.source.local.PokeAppDatabase
import com.example.pokeapp.data.source.local.entity.LocalAbilityDetailsResponse
import com.example.pokeapp.data.source.local.entity.LocalAllAbilitiesResponse
import com.example.pokeapp.data.source.network.PokeAbilityNetworkDataSource
import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.domain.entity.PokeAbilityName
import kotlinx.coroutines.flow.firstOrNull
import java.util.Date
import javax.inject.Inject

class AbilityDataSource @Inject constructor(
    private val pokeAbilityNetworkDataSource: PokeAbilityNetworkDataSource,
    private val pokeAbilityLocalDataSource: PokeAbilityLocalDataSource
) : AbilityRepository {

    override suspend fun getAbilityDetails(abilityName: String): PokeAbility {
        val localData = pokeAbilityLocalDataSource.getDetails(abilityName)
            .firstOrNull()

        return if (
            localData == null ||
            localData.insertionDate.getDifferenceInMinutes(Date()) >= PokeAppDatabase.CACHE_TIME_MINUTES
        ) {
            getAbilityDetailsFromRemote(abilityName)
        } else {
            localData.value.toDomainEntity()
        }
    }

    override suspend fun getAllAbilities(): List<PokeAbilityName> {
        val localData = pokeAbilityLocalDataSource.getAll()
            .firstOrNull()

        return if (
            localData == null ||
            localData.insertionDate.getDifferenceInMinutes(Date()) >= PokeAppDatabase.CACHE_TIME_MINUTES
        ) {
            getAllAbilitiesFromRemote()
        } else {
            localData.value.toDomainEntity()
        }
    }

    private suspend fun getAllAbilitiesFromRemote(): List<PokeAbilityName> {
        val networkResponse = pokeAbilityNetworkDataSource.getAllAbilities()

        pokeAbilityLocalDataSource.insertAll(LocalAllAbilitiesResponse.fromNetworkEntity(networkResponse))

        return networkResponse.toDomainEntity()
    }

    private suspend fun getAbilityDetailsFromRemote(abilityName: String): PokeAbility {
        val networkResponse = pokeAbilityNetworkDataSource.getAbilityData(abilityName)

        pokeAbilityLocalDataSource.insertDetails(LocalAbilityDetailsResponse.fromNetworkEntity(networkResponse))

        return networkResponse.toDomainEntity()
    }
}