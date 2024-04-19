package com.example.pokeapp.domain.entity

import com.example.pokeapp.data.source.network.entity.ApiPokeType

data class PokeType(
    val name: String
) {
    companion object {
        fun fromDataEntity(dataEntity: ApiPokeType) =
            PokeType(
                name = dataEntity.name
            )
    }
}
