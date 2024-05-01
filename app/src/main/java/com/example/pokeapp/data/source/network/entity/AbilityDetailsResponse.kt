package com.example.pokeapp.data.source.network.entity

import com.google.gson.annotations.SerializedName

data class AbilityDetailsResponse(
    val name: String,
    @SerializedName("effect_entries") val entries: List<ApiEffectEntry>
)

data class ApiEffectEntry(
    val language: ApiLanguage,
    val effect: String
)