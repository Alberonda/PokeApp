package com.example.pokeapp.data.source.network.entity

import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.data.source.network.entity.ApiLanguage.Companion.JAPANESE_CODE
import com.example.pokeapp.data.source.network.entity.ApiLanguage.Companion.SPANISH_CODE
import com.example.pokeapp.domain.entity.OtherLanguageName
import com.example.pokeapp.domain.entity.PokeAbility
import com.google.gson.annotations.SerializedName

data class NetworkAbilityDetailsResponse(
    val name: String,
    @SerializedName("effect_entries") val entries: List<ApiEffectEntry>,
    @SerializedName("names") val otherNames: List<ApiNameEntry>
) {

    fun toDomainEntity() =
        PokeAbility(
            name,
            getEnglishEntry()?.effect ?: String.EMPTY,
            otherNames
                .filter { it.language.code in arrayOf(SPANISH_CODE, JAPANESE_CODE) }
                .map { OtherLanguageName(value = it.name, language = it.language.code) }
        )

    private fun getEnglishEntry() =
        entries.find {
            it.language.code == ApiLanguage.ENGLISH_CODE
        }
}

data class ApiEffectEntry(
    val language: ApiLanguage,
    val effect: String
)

data class ApiNameEntry(
    val language: ApiLanguage,
    val name: String
)