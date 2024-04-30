package com.example.pokeapp.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pokeapp.base.Constants.TYPES_NAMES_SEPARATOR
import com.example.pokeapp.presentation.abilitysearch.AbilitySearchScreen
import com.example.pokeapp.presentation.abilitysearch.AbilitySearchScreenViewModel
import com.example.pokeapp.presentation.typeslanding.AllTypesScreen
import com.example.pokeapp.presentation.typedetails.TypeDetailsScreen
import com.example.pokeapp.presentation.typedetails.TypeDetailsScreenViewModel
import com.example.pokeapp.presentation.typeslanding.TypesLandingScreenViewModel


@Composable
fun PokeAppNavHost(
    navController: NavHostController,
    widthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AllTypes.route,
        modifier = modifier
    ) {
        composable(route = AllTypes.route) {
            AllTypesScreen(
                viewModel = hiltViewModel<TypesLandingScreenViewModel>(),
                onGetDetailsClicked = { selectedTypes ->
                    navController.navigateToTypeDetails(
                        selectedTypes.joinToString(
                            TYPES_NAMES_SEPARATOR
                        ) { it.name }
                    )
                },
                onBackNavigation = {
                    navController.popBackStack()
                },
                widthSizeClass = widthSizeClass
            )
        }

        composable(
            route = TypeDetails.routeWithArgs,
            arguments = TypeDetails.arguments
        ) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString(TypeDetails.selectedTypeArg)?.let{
                TypeDetailsScreen(
                    viewModel = hiltViewModel<TypeDetailsScreenViewModel>(),
                    typesToSearch = it,
                    onBackNavigation = {
                        navController.popBackStack()
                    },
                    widthSizeClass = widthSizeClass
                )
            } ?: AllTypesScreen(
                viewModel = hiltViewModel<TypesLandingScreenViewModel>(),
                onGetDetailsClicked = { selectedTypes ->
                    navController.navigateToTypeDetails(
                        selectedTypes.joinToString(
                            TYPES_NAMES_SEPARATOR
                        ) { it.name }
                    )
                },
                onBackNavigation = {
                    navController.popBackStack()
                },
                widthSizeClass = widthSizeClass
            )
        }

        composable(route = AbilitySearch.route){
            AbilitySearchScreen(
                viewModel = hiltViewModel<AbilitySearchScreenViewModel>()
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

private fun NavHostController.navigateToTypeDetails(selectedTypes: String) =
    this.navigateSingleTopTo(
        "${TypeDetails.route}/$selectedTypes"
    )