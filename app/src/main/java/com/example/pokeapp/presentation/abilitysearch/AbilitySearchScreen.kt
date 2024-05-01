package com.example.pokeapp.presentation.abilitysearch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokeapp.R
import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.presentation.abilitysearch.entity.AbilitySearchScreenUiData
import com.example.pokeapp.ui.base.UiState
import com.example.pokeapp.ui.base.components.ErrorRetryDialog

@Composable
fun AbilitySearchScreen(
    viewModel: AbilitySearchScreenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    when(val state = uiState) {
        is UiState.Success ->
            AbilitySearchScreenWithData(
                state.data,
                viewModel::onSearchTextChange,
                viewModel::onToggleSearch
            )

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
                R.string.type_details_error_dialog_subtitle,
                onRetryClicked = { viewModel.getAllAbilitiesData() },
                onDismissClicked = {}
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AbilitySearchScreenWithData(
    screenData : AbilitySearchScreenUiData,
    onSearchTextChange: (String) -> Unit,
    onActiveChange: (Boolean) -> Unit,
) {
    SearchBar(
        query = screenData.searchText,
        onQueryChange = onSearchTextChange,
        onSearch = onSearchTextChange,
        active = screenData.isSearching,
        onActiveChange = onActiveChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LazyColumn {
            items(screenData.suggestedAbilities) { ability ->
                Text(
                    text = ability.name,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 4.dp,
                        end = 8.dp,
                        bottom = 4.dp)
                )
            }
        }
    }
}