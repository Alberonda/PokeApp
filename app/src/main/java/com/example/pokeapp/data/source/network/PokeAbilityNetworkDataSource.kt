package com.example.pokeapp.data.source.network

import com.example.pokeapp.data.source.network.entity.NetworkAbilityDetailsResponse
import com.example.pokeapp.data.source.network.entity.NetworkAllAbilitiesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeAbilityNetworkDataSource {

    @GET("ability/?limit=367")
    suspend fun getAllAbilities(): NetworkAllAbilitiesResponse

    @GET("ability/{abilityName}")
    suspend fun getAbilityData(
        @Path("abilityName") abilityName: String
    ): NetworkAbilityDetailsResponse
}