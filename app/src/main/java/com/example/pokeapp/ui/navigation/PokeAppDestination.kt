package com.example.pokeapp.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Contract for information needed on every Rally navigation destination
 */
interface PokeAppDestination {
    val route: String
}

object AllTypes : PokeAppDestination {
    override val route = "types"
}

object TypeDetails : PokeAppDestination {
    override val route = "types"
    const val selectedTypeArg = "selectedType"

    val routeWithArgs =
        "${route}/{${selectedTypeArg}}"
    val arguments = listOf(
        navArgument(selectedTypeArg) {
            type = NavType.StringType
        }
    )
}
