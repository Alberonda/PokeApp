package com.example.pokeapp.presentation.abilitieslanding

import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.domain.entity.PokeAbilityName
import com.example.pokeapp.domain.usecase.getallabilities.GetAbilityDetailsUseCase
import com.example.pokeapp.domain.usecase.getallabilities.GetAllAbilitiesUseCase
import com.example.pokeapp.ui.base.UiState
import com.example.pokeapp.utils.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class AbilitiesLandingScreenViewModelTest {

    private lateinit var viewModel: AbilitiesLandingScreenViewModel

    private val getAllAbilitiesUseCase: GetAllAbilitiesUseCase = mockk()
    private val getAbilityDetailsUseCase: GetAbilityDetailsUseCase = mockk()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `getAllAbilitiesData called on init with success response from useCase`() = runTest {
        // Given
        val useCaseResponse = mockGetAllAbilitiesUseCaseSuccessResponse()

        coEvery { getAllAbilitiesUseCase.execute() } returns useCaseResponse

        val expectedUiState = UiState.Success(
            AbilitiesLandingScreenUiData(
                isSearching = false,
                searchText = String.EMPTY,
                suggestedAbilities = useCaseResponse.getOrNull()!!
            )
        )

        // When
        instanceViewModel()

        // Then
        coVerify(exactly = 1) {
            getAllAbilitiesUseCase.execute()
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `getAllAbilitiesData with failure response from useCase`() = runTest {
        // Given
        val useCaseResponse = mockUseCaseFailureResponse<List<PokeAbilityName>>()

        coEvery { getAllAbilitiesUseCase.execute() } returns useCaseResponse

        val expectedUiState = UiState.Error()

        // When
        instanceViewModel()

        // Then
        coVerify(exactly = 1) {
            getAllAbilitiesUseCase.execute()
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `getAbilityDetails called on init with success response from useCase`() = runTest {
        // Given
        val abilityToSearch = "ability 1"
        val useCaseResponse = mockGetAbilityDetailsUseCaseSuccessResponse()

        coEvery { getAbilityDetailsUseCase.execute(abilityToSearch) } returns useCaseResponse

        val expectedUiState = UiState.Success(
            AbilitiesLandingScreenUiData(
                isSearching = false,
                searchText = String.EMPTY,
                suggestedAbilities = emptyList(),
                selectedAbilityData = useCaseResponse.getOrNull()
            )
        )

        // When
        instanceViewModel()
        viewModel.getAbilityDetails(abilityToSearch)

        // Then
        coVerify(exactly = 1) {
            getAbilityDetailsUseCase.execute(abilityToSearch)
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `getAbilityDetails with failure response from useCase`() = runTest {
        // Given
        val abilityToSearch = "ability 1"
        val useCaseResponse = mockUseCaseFailureResponse<PokeAbility>()

        coEvery { getAbilityDetailsUseCase.execute(abilityToSearch) } returns useCaseResponse

        val expectedUiState = UiState.Error()

        // When
        instanceViewModel()
        viewModel.getAbilityDetails(abilityToSearch)

        // Then
        coVerify(exactly = 1) {
            getAbilityDetailsUseCase.execute(abilityToSearch)
        }

        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `onSearchTextChange with success ui state`() = runTest {
        // Given
        val stringToSearch = "abi"

        val useCaseResponse = Result.success(
            listOf(
                PokeAbilityName("ability 1"),
                PokeAbilityName("blablabla 2"),
                PokeAbilityName("ability 3")
            )
        )

        coEvery { getAllAbilitiesUseCase.execute() } returns useCaseResponse

        instanceViewModel()
        val expectedUiState = UiState.Success(
            AbilitiesLandingScreenUiData(
                isSearching = (viewModel.uiState.value as UiState.Success).data.isSearching,
                searchText = stringToSearch,
                suggestedAbilities = listOf(
                    PokeAbilityName("ability 1"),
                    PokeAbilityName("ability 3")
                ),
            )
        )

        // When
        viewModel.onSearchTextChange(stringToSearch)

        // Then
        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `onSearchTextChange without success ui state`() = runTest {
        // Given
        val stringToSearch = "abi"

        coEvery { getAllAbilitiesUseCase.execute() } returns mockUseCaseFailureResponse()

        instanceViewModel()
        val expectedUiState = UiState.Error()

        // When
        viewModel.onSearchTextChange(stringToSearch)

        // Then
        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `onToggleSearch with success ui state`() = runTest {
        // Given
        val toggleSearch = true

        val useCaseResponse = Result.success(
            listOf(
                PokeAbilityName("ability 1"),
                PokeAbilityName("blablabla 2"),
                PokeAbilityName("ability 3")
            )
        )

        coEvery { getAllAbilitiesUseCase.execute() } returns useCaseResponse

        instanceViewModel()
        val expectedUiState = UiState.Success(
            AbilitiesLandingScreenUiData(
                isSearching = toggleSearch,
                searchText = String.EMPTY,
                suggestedAbilities = useCaseResponse.getOrNull()!!
            )
        )

        // When
        viewModel.onToggleSearch(toggleSearch)

        // Then
        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }

    @Test
    fun `onToggleSearch without success ui state`() = runTest {
        // Given
        val toggleSearch = true

        coEvery { getAllAbilitiesUseCase.execute() } returns mockUseCaseFailureResponse()

        instanceViewModel()
        val expectedUiState = UiState.Error()

        // When
        viewModel.onToggleSearch(toggleSearch)

        // Then
        Assert.assertEquals(
            expectedUiState,
            viewModel.uiState.value
        )
    }


    /*
    fun onToggleSearch(selectedSearch: Boolean) {
        (uiState.value as? UiState.Success)?.data?.let { currentState ->
            _uiState.value = UiState.Success(
                AbilitiesLandingScreenUiData(
                isSearching = selectedSearch,
                searchText = currentState.searchText,
                suggestedAbilities = currentState.suggestedAbilities,
            )
            )
            if (!currentState.isSearching) {
                onSearchTextChange("")
            }
        }
    }
     */

    private fun instanceViewModel() {
        viewModel = AbilitiesLandingScreenViewModel(
            getAllAbilitiesUseCase,
            getAbilityDetailsUseCase,
            coroutineRule.testDispatcher
        )
    }

    private fun mockGetAllAbilitiesUseCaseSuccessResponse() =
        Result.success(
            listOf(
                PokeAbilityName("ability 1"),
                PokeAbilityName("ability 2")
            )
        )

    private fun mockGetAbilityDetailsUseCaseSuccessResponse() =
        Result.success(
            PokeAbility("ability 1", "ability description")
        )

    private fun <T> mockUseCaseFailureResponse() =
        Result.failure<T>(
            Exception("Use case exception")
        )
}