package com.example.pokeapp.presentation.abilitylanding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pokeapp.R
import com.example.pokeapp.base.capitalizeValue
import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.ui.base.UiState
import com.example.pokeapp.ui.base.components.ErrorRetryDialog

@Composable
fun AbilityLandingScreen(
    viewModel: AbilityLandingScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    when(val state = uiState) {
        is UiState.Success ->
            AbilitySearchScreenWithData(
                state.data,
                viewModel::onSearchTextChange,
                viewModel::onToggleSearch
            ) {
                viewModel.getAbilityDetails(it.name)
            }

        is UiState.Loading -> {
            Box(modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        is UiState.Error ->
            ErrorRetryDialog(
                R.string.ability_search_error_dialog_subtitle,
                onRetryClicked = { viewModel.getAllAbilitiesData() },
                onDismissClicked = {}
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbilitySearchScreenWithData(
    screenData : AbilityLandingScreenUiData,
    onSearchTextChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
    onItemClicked: (PokeAbility) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        SearchBar(
            query = screenData.searchText,
            onQueryChange = onSearchTextChange,
            onSearch = onSearchTextChange,
            active = screenData.isSearching,
            onActiveChange = onActiveChange,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LazyColumn {
                items(screenData.suggestedAbilities) { ability ->
                    Text(
                        text = ability.name,
                        modifier = Modifier
                            .padding(
                                start = 8.dp,
                                top = 4.dp,
                                end = 8.dp,
                                bottom = 4.dp
                            )
                            .fillMaxWidth()
                            .clickable {
                                onItemClicked(ability)
                            }
                    )
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onTertiary,
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp)
        ) {
            Surface(
                modifier = Modifier.background(Color.Red)
            ) {
                screenData.selectedAbilityData?.let {
                    AbilityDetailsContainer(
                        screenData.selectedAbilityData
                    )
                } ?: AbilityDetailsDefaultContainer()
            }
        }
    }
}

@Composable
fun AbilityDetailsContainer(
    abilityDetails: PokeAbility
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            text = abilityDetails.name.capitalizeValue(),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.effect_title),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        Text(
            text = abilityDetails.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}

@Composable
fun AbilityDetailsDefaultContainer() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxHeight()
    ) {
        Image(
            painter = painterResource(R.drawable.ic_magnifying_glass),
            contentDescription = "Search image",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
            modifier = Modifier
                .size(300.dp)
                .fillMaxSize()
        )
        Text(
            text = stringResource(R.string.ability_search_placeholder),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(8.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

}