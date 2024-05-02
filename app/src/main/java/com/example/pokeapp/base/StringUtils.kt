package com.example.pokeapp.base

import java.util.Locale

const val SLASH = "/"
val String.Companion.EMPTY get() = ""

fun String.capitalizeValue() = replaceFirstChar {
    if (it.isLowerCase()) {
        it.titlecase(Locale.getDefault())
    } else {
        it.toString()
    }
}
