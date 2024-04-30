package com.example.pokeapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.pokeapp.ui.base.components.CustomIcons

/**
 * Contract for information needed on every Rally navigation destination
 */
interface PokeAppDestination {
    val route: String
}

interface PokeAppTabDestination: PokeAppDestination {
    val icon: ImageVector
}

object AllTypes : PokeAppTabDestination {
    override val icon = CustomIcons.TypeSpecimenIcon
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

object AbilitySearch : PokeAppTabDestination {
    override val icon = Icons.Filled.Search
    override val route = "abilities"
}

val tabRowScreens = listOf<PokeAppTabDestination>(AllTypes, AbilitySearch)

