package com.example.pokeapp.data.source.network

import com.example.pokeapp.data.source.network.entity.AllTypesResponse
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeTypeNetworkDataSource {

    @GET("type")
    suspend fun getAllTypes(): AllTypesResponse

    @GET("type/{typeName}")
    suspend fun getTypeData(
        @Path("typeName") typeName: String
    ): TypeDetailsResponse
}