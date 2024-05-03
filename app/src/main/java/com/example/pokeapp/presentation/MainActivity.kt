package com.example.pokeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.PokeAppTheme
import com.example.pokeapp.R
import com.example.pokeapp.ui.base.components.PokeAppTabRow
import com.example.pokeapp.ui.navigation.AllTypes
import com.example.pokeapp.ui.navigation.PokeAppNavHost
import com.example.pokeapp.ui.navigation.navigateSingleTopTo
import com.example.pokeapp.ui.navigation.tabRowScreens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            PokeApp(windowSizeClass)
        }
    }
}

@Composable
fun PokeApp(windowSize: WindowSizeClass) {
    PokeAppTheme {
        val navController = rememberNavController()

        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination?.route

        val currentScreen = tabRowScreens.find {
            currentDestination?.contains(it.route) ?: false
        } ?: AllTypes

        Scaffold(
            topBar = {
                PokeAppTabRow(
                    allScreens = tabRowScreens,
                    onTabSelected = { screen ->
                        navController.navigateSingleTopTo(screen.route)
                    },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val backgroundResource = if (isSystemInDarkTheme()) {
                        R.drawable.app_background_dark
                    } else {
                        R.drawable.app_background_light
                    }

                    Image(
                        painter = painterResource(id = backgroundResource),
                        contentDescription = "App background",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.matchParentSize()
                    )
                    PokeAppNavHost(
                        navController,
                        windowSize.widthSizeClass,
                        Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
