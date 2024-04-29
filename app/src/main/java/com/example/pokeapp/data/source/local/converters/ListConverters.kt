package com.example.pokeapp.data.source.local.converters

import androidx.room.TypeConverter
import com.example.pokeapp.data.source.local.entity.PokeType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object ListConverters {
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
}