package com.example.pokeapp.data.source.network.entity

import com.example.pokeapp.domain.entity.PokeAbility
import com.google.gson.annotations.SerializedName

data class NetworkAbilityDetailsResponse(
    val name: String,
    @SerializedName("effect_entries") val entries: List<ApiEffectEntry>
) {

    fun toDomainEntity() = this.getEnglishEntry()?.run {
        PokeAbility(
            name,
            effect
        )
    } ?: PokeAbility(name)

    private fun getEnglishEntry() =
        entries.find {
            it.language.code == ApiLanguage.ENGLISH_CODE
        }
}

data class ApiEffectEntry(
    val language: ApiLanguage,
    val effect: String
)