package com.example.pokeapp.presentation.abilitieslanding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.di.DefaultDispatcher
import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.domain.entity.PokeAbilityName
import com.example.pokeapp.domain.usecase.getallabilities.GetAbilityDetailsUseCase
import com.example.pokeapp.domain.usecase.getallabilities.GetAllAbilitiesUseCase
import com.example.pokeapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AbilitiesLandingScreenViewModel @Inject constructor(
    private val getAllAbilitiesUseCase: GetAllAbilitiesUseCase,
    private val getAbilityDetailsUseCase: GetAbilityDetailsUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState:
            MutableStateFlow<UiState<AbilitiesLandingScreenUiData>> = MutableStateFlow(
                UiState.Loading
            )
    val uiState: StateFlow<UiState<AbilitiesLandingScreenUiData>> = _uiState.asStateFlow()

    private var allAbilities = emptyList<PokeAbilityName>()

    init {
        getAllAbilitiesData()
    }

    fun getAllAbilitiesData() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                withContext(defaultDispatcher) {
                    getAllAbilitiesUseCase.execute()
                }.run {
                    if (this.isSuccess) {
                        allAbilities = this.getOrDefault(emptyList())
                        _uiState.value =
                            UiState.Success(
                                AbilitiesLandingScreenUiData(
                                    isSearching = false,
                                    searchText = String.EMPTY,
                                    suggestedAbilities = getSuggestedAbilities(String.EMPTY),
                                )
                            )
                    } else {
                        _uiState.value = UiState.Error()
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error()
            }
        }
    }

    fun onSearchTextChange(text: String) {
        (uiState.value as? UiState.Success)?.let { currentState ->
            _uiState.value = UiState.Success(
                    AbilitiesLandingScreenUiData(
                    isSearching = currentState.data.isSearching,
                    searchText = text,
                    suggestedAbilities = getSuggestedAbilities(text),
                )
            )
        }
    }

    fun onToggleSearch(selectedSearch: Boolean) {
        (uiState.value as? UiState.Success)?.let { currentState ->
            _uiState.value = UiState.Success(
                    AbilitiesLandingScreenUiData(
                    isSearching = selectedSearch,
                    searchText = String.EMPTY,
                    suggestedAbilities = getSuggestedAbilities(String.EMPTY),
                )
            )
        }
    }

    private fun getSuggestedAbilities(searchText: String): List<PokeAbilityName> =
        allAbilities.filter { ability ->
            ability.value.uppercase().contains(searchText.trim().uppercase())
        }

    fun getAbilityDetails(abilityToSearch: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                withContext(defaultDispatcher) {
                    getAbilityDetailsUseCase.execute(abilityToSearch)
                }.run {
                    if (this.isSuccess) {
                        _uiState.value =
                            UiState.Success(
                                AbilitiesLandingScreenUiData(
                                    isSearching = false,
                                    searchText = String.EMPTY,
                                    suggestedAbilities = getSuggestedAbilities(String.EMPTY),
                                    selectedAbilityData = getOrDefault(
                                        PokeAbility(
                                            abilityToSearch,
                                            "Error"
                                        )
                                    )
                                )
                            )
                    } else {
                        _uiState.value = UiState.Error()
                    }
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error()
            }
        }
    }
}