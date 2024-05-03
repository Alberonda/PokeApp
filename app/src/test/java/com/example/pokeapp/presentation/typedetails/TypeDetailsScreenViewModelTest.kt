package com.example.pokeapp.presentation.typedetails

import androidx.lifecycle.SavedStateHandle
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import com.example.pokeapp.domain.usecase.gettypedetails.GetTypeDetailsUseCase
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiResources
import com.example.pokeapp.ui.base.UiState
import com.example.pokeapp.ui.navigation.TypeDetails
import com.example.pokeapp.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class TypeDetailsScreenViewModelTest {

    private lateinit var viewModel: TypeDetailsScreenViewModel

    private val savedStateHandle: SavedStateHandle = mockk()
    private val getTypeDetailsUseCase: GetTypeDetailsUseCase = mockk()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `getAllTypesData called on init with success response from useCase`() = runTest {
        // Given
        val typesToSearch = "ground, dragon, type 3"

        every { savedStateHandle.get<String>(TypeDetails.selectedTypeArg) } returns typesToSearch

        coEvery { getTypeDetailsUseCase.execute(any()) } answers {
            getMockedSuccessUseCaseResponse()
        }

        val expectedUiState = UiState.Success(
            TypeDetailsScreenUiData(
                searchedTypes= listOf(
                    PokeTypeUiData(PokeTypeUiResources.GROUND),
                    PokeTypeUiData(PokeTypeUiResources.DRAGON)
                ),
                unaffectedRelations = listOf(
                    PokeTypeUiData(PokeTypeUiResources.ELECTRIC),
                ),
                effectiveRelations = listOf(
                    PokeTypeUiData(PokeTypeUiResources.DRAGON),
                    PokeTypeUiData(PokeTypeUiResources.FAIRY)
                ),
                notEffectiveRelations = listOf(
                    PokeTypeUiData(PokeTypeUiResources.WATER),
                    PokeTypeUiData(PokeTypeUiResources.FIRE),
                    PokeTypeUiData(PokeTypeUiResources.NORMAL)
                ),
                veryNotEffectiveRelations = listOf(
                    PokeTypeUiData(PokeTypeUiResources.GROUND)
                )
            )
        )

        // When
        instanceViewModel()

        // Then
        coVerify(exactly = 1) {
            getTypeDetailsUseCase.execute(listOf("ground", "dragon", "type 3"))
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `getAllTypesData with zero types to search`() = runTest {
        // Given
        val typesToSearch = ""

        every { savedStateHandle.get<String>(TypeDetails.selectedTypeArg) } returns typesToSearch

        val expectedUiState = UiState.Error()

        // When
        instanceViewModel()

        // Then
        coVerify(exactly = 0) {
            getTypeDetailsUseCase.execute(any())
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `getAllTypesData with one type to search and failure response from useCase`() = runTest {
        // Given
        val typesToSearch = "type 1"
        val useCaseResponse = getMockedFailureUseCaseResponse()

        every { savedStateHandle.get<String>(TypeDetails.selectedTypeArg) } returns typesToSearch
        coEvery { getTypeDetailsUseCase.execute(listOf("type 1")) } returns useCaseResponse

        val expectedUiState = UiState.Error()

        // When
        instanceViewModel()

        // Then
        coVerify(exactly = 1) {
            getTypeDetailsUseCase.execute(listOf("type 1"))
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    private fun instanceViewModel() {
        viewModel = TypeDetailsScreenViewModel(
            savedStateHandle,
            getTypeDetailsUseCase,
            coroutineRule.testDispatcher
        )
    }

    private fun getMockedSuccessUseCaseResponse() =
        Result.success(
            PokeTypeDetails(
                name = "dragon",
                attackerTypesRelation = listOf(
                    Pair(PokeType("dragon"), 4.0),
                ),
                defenderTypesRelation = listOf(
                    Pair(PokeType("dragon"), 2.0),
                    Pair(PokeType("fairy"), 2.0),
                    Pair(PokeType("flying"), 1.0),
                    Pair(PokeType("water"), 0.5),
                    Pair(PokeType("fire"), 0.5),
                    Pair(PokeType("normal"), 0.5),
                    Pair(PokeType("electric"), 0.0),
                    Pair(PokeType("ground"), 0.25),
                ),
            )
        )

    private fun getMockedFailureUseCaseResponse() =
        Result.failure<PokeTypeDetails>(
            Exception("Use case exception")
        )
}