package com.example.pokeapp.presentation.typedetails

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.PokeAppTheme
import com.example.pokeapp.R
import com.example.pokeapp.base.SLASH
import com.example.pokeapp.base.ErrorDialog
import com.example.pokeapp.presentation.typedetails.entity.TypeDetailsScreenUiData
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiResources

@Composable
fun TypeDetailsScreen(
    viewModel: TypeDetailsScreenViewModel,
    onBackNavigation: () -> Unit,
    typesToSearch: String,
    modifier: Modifier = Modifier,
    widthSizeClass: WindowWidthSizeClass
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.typesDetails != null ->
            uiState.typesDetails?.let {
                TypeDetailsContent(
                    it,
                    widthSizeClass = widthSizeClass
                )
            }

        uiState.isLoading -> {
            Box(modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        else ->
            ErrorDialog(
                R.string.type_details_error_dialog_subtitle,
                onDismissClicked = onBackNavigation
            )
    }
}

@Composable
fun TypeDetailsContent(
    uiData: TypeDetailsScreenUiData,
    modifier: Modifier = Modifier,
    widthSizeClass: WindowWidthSizeClass
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        TypeDetailsSearchedTitle(
            R.string.type_details_header,
            uiData.searchedTypes.map {
                stringResource(id = it.resources.localisedName)
            }.joinToString(SLASH),
            modifier = modifier.weight(
                if (widthSizeClass == WindowWidthSizeClass.Compact) 0.1f else 0.2f
            )
        )

        Column(
            modifier = modifier
                .weight(
                    if (widthSizeClass == WindowWidthSizeClass.Compact) 0.9f else 0.8f,
                    fill = false
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TypeRelationsCard(
                R.string.very_weak_against,
                uiData.veryEffectiveRelations
            )
            TypeRelationsCard(
                R.string.weak_against,
                uiData.effectiveRelations
            )
            TypeRelationsCard(
                R.string.resistant_against,
                uiData.notEffectiveRelations
            )
            TypeRelationsCard(
                R.string.very_resistant_against,
                uiData.veryNotEffectiveRelations
            )
            TypeRelationsCard(
                R.string.immune_against,
                uiData.unaffectedRelations
            )
        }
    }
}

@Composable
fun TypeDetailsSearchedTitle(
    @StringRes title: Int,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(text = stringResource(title), style = MaterialTheme.typography.titleLarge)
        Text(text = subtitle, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun TypeRelationsCard(
    @StringRes title: Int,
    cardData: List<PokeTypeUiData>,
    modifier: Modifier = Modifier
) {
    if (cardData.isNotEmpty()) {
        Card(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(title),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.headlineSmall
            )
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
            ) {
                items(cardData.size) { index ->
                    PokeTypeSmallCard(
                        cardData[index]
                    )
                }
            }
        }
    }
}

@Composable
fun PokeTypeSmallCard(
    pokeType: PokeTypeUiData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Row(
            modifier = modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            colorResource(pokeType.resources.color)
                        )
                    )
                )
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(pokeType.resources.imageResource),
                contentDescription = "Type image",
                modifier = Modifier
                    .size(30.dp)
                    .fillMaxSize()
            )
            Text(
                text = stringResource(pokeType.resources.localisedName).uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TypeDetailsSearchedTitlePreview() {
    PokeAppTheme {
        TypeDetailsSearchedTitle(R.string.type_details_header, "Tipo AAABBB")
    }
}

@Preview
@Composable
private fun TypeDetailsContentPreview() {
    PokeAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            TypeDetailsContent(
                TypeDetailsScreenUiData(
                    searchedTypes = listOf(
                        PokeTypeUiData(PokeTypeUiResources.BUG),
                        PokeTypeUiData(PokeTypeUiResources.ROCK),
                    ),
                    veryEffectiveRelations = listOf(
                        PokeTypeUiData(PokeTypeUiResources.WATER),
                        PokeTypeUiData(PokeTypeUiResources.GRASS),
                    ),
                    effectiveRelations = listOf(
                        PokeTypeUiData(PokeTypeUiResources.DRAGON),
                        PokeTypeUiData(PokeTypeUiResources.WATER),
                        PokeTypeUiData(PokeTypeUiResources.GRASS),
                    ),
                    notEffectiveRelations = listOf(
                        PokeTypeUiData(PokeTypeUiResources.DRAGON),
                        PokeTypeUiData(PokeTypeUiResources.FAIRY),
                        PokeTypeUiData(PokeTypeUiResources.FIGHTING),
                        PokeTypeUiData(PokeTypeUiResources.FIRE),
                    ),
                    veryNotEffectiveRelations = listOf(
                        PokeTypeUiData(PokeTypeUiResources.DRAGON),
                        PokeTypeUiData(PokeTypeUiResources.FAIRY),
                        PokeTypeUiData(PokeTypeUiResources.FIGHTING),
                        PokeTypeUiData(PokeTypeUiResources.FIRE),
                        PokeTypeUiData(PokeTypeUiResources.ELECTRIC),
                    ),
                    unaffectedRelations = listOf()
                ),
                widthSizeClass = WindowWidthSizeClass.Compact
            )
        }
    }
}