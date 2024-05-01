package com.example.pokeapp.data.source.network

import com.example.pokeapp.data.source.network.entity.NetworkAllAbilitiesResponse
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeAbilityNetworkDataSource {

    @GET("ability/?limit=367")
    suspend fun getAllAbilities(): NetworkAllAbilitiesResponse

    @GET("ability/{abilityName}")
    suspend fun getAbilityData(
        @Path("typeName") typeName: String
    ): TypeDetailsResponse
}