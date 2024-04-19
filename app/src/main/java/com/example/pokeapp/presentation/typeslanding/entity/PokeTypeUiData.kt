package com.example.pokeapp.presentation.typeslanding.entity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pokeapp.domain.entity.PokeType

data class PokeTypeUiData(
    val resources: PokeTypeUiResources
) {
    var name = resources.typeName
    var selected by mutableStateOf(false)

    companion object {
        fun mapDomainToUiEntities(domainTypes: List<PokeType>) =
            domainTypes.mapNotNull { domainEntity ->
                mapDomainToUiResources(domainEntity) ?.let { resources ->
                    PokeTypeUiData(
                        resources = resources
                    )
                }
            }

        fun mapDomainToUiResources(domainEntity: PokeType) =
            PokeTypeUiResources.entries.firstOrNull {
                it.typeName == domainEntity.name.lowercase()
            }
    }
}