package com.example.pokeapp.data.source.network

import com.example.pokeapp.data.source.network.entity.AllTypesResponse
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeTypeNetworkDataSource {

    @GET("api/v2/type")
    suspend fun getAllTypes(): AllTypesResponse

    @GET("api/v2/type/{typeName}")
    suspend fun getTypeData(
        @Path("typeName") typeName: String
    ): TypeDetailsResponse
}