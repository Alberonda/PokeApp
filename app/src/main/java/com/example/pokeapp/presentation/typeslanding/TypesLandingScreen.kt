package com.example.pokeapp.presentation.typeslanding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.pokeapp.base.CardFace
import com.example.pokeapp.base.quantityStringResource
import com.example.pokeapp.base.ErrorRetryDialog
import com.example.pokeapp.base.FlipCard
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiResources

@Composable
fun AllTypesScreen(
    viewModel: TypesLandingScreenViewModel,
    modifier: Modifier = Modifier,
    onGetDetailsClicked: (List<PokeTypeUiData>) -> Unit,
    onBackNavigation: () -> Unit,
    widthSizeClass: WindowWidthSizeClass
) {
    val uiState by viewModel.uiState.collectAsState()

    val maxSelectableTypes = 2

    when {
        uiState.allTypes.isNotEmpty() ->
            AllTypesScreenWithData(
                uiState.allTypes,
                onCardClicked = { pokeType ->
                    val numberOfSelected = uiState.allTypes.filter { it.selected }.size
                    if (pokeType.selected || numberOfSelected < maxSelectableTypes) {
                        pokeType.selected = !pokeType.selected
                        true
                    } else {
                        false
                    }
                },
                onGetDetailsClicked = {
                    onGetDetailsClicked(
                        uiState.allTypes.filter { it.selected }
                    )
                },
                widthSizeClass = widthSizeClass
            )

        uiState.isLoading -> {
            Box(modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        else ->
            ErrorRetryDialog(
                R.string.type_details_error_dialog_subtitle,
                onRetryClicked = {
                    viewModel.getAllTypesData()
                },
                onDismissClicked = onBackNavigation
            )
    }
}

@Composable
fun AllTypesScreenWithData(
    allTypes: List<PokeTypeUiData>,
    modifier: Modifier = Modifier,
    onCardClicked: (PokeTypeUiData) -> Boolean = { false },
    onGetDetailsClicked: () -> Unit = {},
    widthSizeClass: WindowWidthSizeClass
) {
    Column {
        AllTypesContent(
            allTypes,
            onCardClicked = onCardClicked,
            modifier = modifier.weight(
                if (widthSizeClass == WindowWidthSizeClass.Compact) 0.9f else 0.8f
            )
        )
        GetTypeDetailsButton(
            onGetDetailsClicked = onGetDetailsClicked,
            numberOfSelectedTypes = allTypes.filter { it.selected }.size,
            modifier = modifier.weight(
                if (widthSizeClass == WindowWidthSizeClass.Compact) 0.1f else 0.2f
            )
        )
    }
}

@Composable
fun GetTypeDetailsButton(
    modifier: Modifier = Modifier,
    numberOfSelectedTypes: Int,
    onGetDetailsClicked: () -> Unit = {}
) {
    val enabled = numberOfSelectedTypes > 0

    val animatedButtonColor by animateColorAsState(
        targetValue = if (enabled) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.secondary
        },
        animationSpec = tween(700, 0, LinearEasing), label = ""
    )

    Button(
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 18.dp)
            .padding(bottom = 18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedButtonColor,
            disabledContainerColor = animatedButtonColor
        ),
        onClick = {
            onGetDetailsClicked()
        }
    ) {
        Text(
            text = if (enabled) {
                quantityStringResource(R.plurals.get_type_details_button, numberOfSelectedTypes)
            } else {
                stringResource(id = R.string.no_type_selected)
            },
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = Color.White,
        )
    }
}

@Composable
fun AllTypesContent(
    allTypes: List<PokeTypeUiData>,
    modifier: Modifier = Modifier,
    onCardClicked: (PokeTypeUiData) -> Boolean = { false }
) {
    LazyVerticalGrid(
        modifier = modifier
            .padding(vertical = 6.dp, horizontal = 18.dp),
        columns = GridCells.Adaptive(minSize = 128.dp),
        content = {
            items(allTypes.size) { index ->
                PokeTypeCard(
                    allTypes[index],
                    onCardClicked = onCardClicked
                )
            }
        }
    )
}

@Composable
fun PokeTypeCard(
    pokeType: PokeTypeUiData,
    modifier: Modifier = Modifier,
    onCardClicked: (PokeTypeUiData) -> Boolean = { false },
) {
    var cardFace by rememberSaveable {
        mutableStateOf(CardFace.Front)
    }

    FlipCard(
        cardFace = cardFace,
        onClick = {
            if (onCardClicked(pokeType)) {
                cardFace = it.next
            }
        },
        back = {
            PokeCard(
                pokeType,
                modifier
            )
        },
        front = {
            PokeCard(
                pokeType,
                modifier
            )
        }
    )
}

@Composable
fun PokeCard(
    pokeType: PokeTypeUiData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        colorResource(pokeType.resources.color)
                    )
                )
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if (!pokeType.selected) {
                painterResource(pokeType.resources.imageResource)
            } else {
                painterResource(R.drawable.ic_checked)
            },
            contentDescription = "Type image",
            modifier = Modifier
                .size(60.dp)
                .fillMaxSize()
        )
        Text(
            text = stringResource(pokeType.resources.localisedName).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Preview
@Composable
private fun AllTypesScreenWithDataPortraitPreview() {
    PokeAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            AllTypesScreenWithData(
                listOf(
                    PokeTypeUiData(PokeTypeUiResources.DRAGON),
                    PokeTypeUiData(PokeTypeUiResources.FAIRY),
                    PokeTypeUiData(PokeTypeUiResources.FIGHTING),
                    PokeTypeUiData(PokeTypeUiResources.FIRE),
                    PokeTypeUiData(PokeTypeUiResources.ELECTRIC),
                    PokeTypeUiData(PokeTypeUiResources.DRAGON),
                    PokeTypeUiData(PokeTypeUiResources.WATER),
                    PokeTypeUiData(PokeTypeUiResources.GRASS),
                ),
                widthSizeClass = WindowWidthSizeClass.Compact
            )
        }
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun AllTypesScreenWithDataLandscapePreview() {
    PokeAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            AllTypesScreenWithData(
                listOf(
                    PokeTypeUiData(PokeTypeUiResources.DRAGON),
                    PokeTypeUiData(PokeTypeUiResources.FAIRY),
                    PokeTypeUiData(PokeTypeUiResources.FIGHTING),
                    PokeTypeUiData(PokeTypeUiResources.FIRE),
                    PokeTypeUiData(PokeTypeUiResources.ELECTRIC),
                    PokeTypeUiData(PokeTypeUiResources.DRAGON),
                    PokeTypeUiData(PokeTypeUiResources.WATER),
                    PokeTypeUiData(PokeTypeUiResources.GRASS),
                ),
                widthSizeClass = WindowWidthSizeClass.Compact
            )
        }
    }
}