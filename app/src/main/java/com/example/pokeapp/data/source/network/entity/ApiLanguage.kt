package com.example.pokeapp.data.source.network.entity

import com.google.gson.annotations.SerializedName

data class ApiLanguage(
    @SerializedName("name") val code: String,
    val url: String
) {
    companion object {
        const val ENGLISH_CODE = "en"
    }
}
