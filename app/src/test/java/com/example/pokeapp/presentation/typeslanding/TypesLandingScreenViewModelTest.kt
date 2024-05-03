package com.example.pokeapp.presentation.typeslanding

import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.usecase.getalltypes.GetAllTypesUseCase
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData
import com.example.pokeapp.ui.base.UiState
import com.example.pokeapp.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class TypesLandingScreenViewModelTest {

    private lateinit var viewModel: TypesLandingScreenViewModel

    private val getAllTypesUseCase: GetAllTypesUseCase = mockk()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `getAllTypesData called on init with success response from useCase`() = runTest {
        // Given
        val useCaseResponse = getMockedSuccessUseCaseResponse()

        coEvery { getAllTypesUseCase.execute() } returns useCaseResponse

        val expectedUiState = UiState.Success(
            TypesLandingScreenUiData(
                PokeTypeUiData.mapDomainToUiEntities(
                    useCaseResponse.getOrDefault(emptyList())
                )
            )
        )

        // When
        instanceViewModel()

        // Then
        coVerify(exactly = 1) {
            getAllTypesUseCase.execute()
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `getAllTypesData with failure response from useCase`() = runTest {
        // Given
        val useCaseResponse = getMockedFailureUseCaseResponse()

        coEvery { getAllTypesUseCase.execute() } returns useCaseResponse

        val expectedUiState = UiState.Error()

        // When
        instanceViewModel()

        // Then
        coVerify(exactly = 1) {
            getAllTypesUseCase.execute()
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    private fun instanceViewModel() {
        viewModel = TypesLandingScreenViewModel(
            getAllTypesUseCase,
            coroutineRule.testDispatcher
        )
    }

    private fun getMockedSuccessUseCaseResponse() =
        Result.success(
            listOf(
                PokeType("type 1"),
                PokeType("type 2")
            )
        )

    private fun getMockedFailureUseCaseResponse() =
        Result.failure<List<PokeType>>(
            Exception("Use case exception")
        )
}