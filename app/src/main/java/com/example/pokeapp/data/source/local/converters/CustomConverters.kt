package com.example.pokeapp.data.source.local.converters

import androidx.room.TypeConverter
import com.example.pokeapp.data.source.local.entity.PokeType
import com.example.pokeapp.data.source.network.entity.NetworkAllAbilitiesResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object CustomConverters {
    private inline fun <reified T> Gson.fromJson(json: String): T =
        fromJson(json, object : TypeToken<T>() {}.type)

    @TypeConverter
    fun fromPokeTypeList(value: List<PokeType>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toPokeTypeList(value: String): List<PokeType> {
        return try {
            Gson().fromJson<List<PokeType>>(value)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    @TypeConverter
    fun fromNetworkAllAbilitiesResponse(value: NetworkAllAbilitiesResponse): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toNetworkAllAbilitiesResponse(value: String): NetworkAllAbilitiesResponse {
        return try {
            Gson().fromJson<NetworkAllAbilitiesResponse>(value)
        } catch (e: Exception) {
            NetworkAllAbilitiesResponse(emptyList())
        }
    }


}