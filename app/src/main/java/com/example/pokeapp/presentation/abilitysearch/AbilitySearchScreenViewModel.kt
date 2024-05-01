package com.example.pokeapp.presentation.abilitysearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.di.DefaultDispatcher
import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.domain.usecase.getallabilities.GetAllAbilitiesUseCase
import com.example.pokeapp.presentation.abilitysearch.entity.AbilitySearchScreenUiData
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
class AbilitySearchScreenViewModel @Inject constructor(
    private val getAllAbilitiesUseCase: GetAllAbilitiesUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState:
            MutableStateFlow<UiState<AbilitySearchScreenUiData>> = MutableStateFlow(
                UiState.Loading
            )
    val uiState: StateFlow<UiState<AbilitySearchScreenUiData>> = _uiState.asStateFlow()

    private var allAbilities = emptyList<PokeAbility>()

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
                                AbilitySearchScreenUiData(
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
        (uiState.value as? UiState.Success)?.data?.let { currentState ->
            _uiState.value = UiState.Success(AbilitySearchScreenUiData(
                isSearching = currentState.isSearching,
                searchText = text,
                suggestedAbilities = getSuggestedAbilities(text),
            ))
        }
    }

    fun onToggleSearch(selectedSearch: Boolean) {
        (uiState.value as? UiState.Success)?.data?.let { currentState ->
            _uiState.value = UiState.Success(AbilitySearchScreenUiData(
                isSearching = selectedSearch,
                searchText = currentState.searchText,
                suggestedAbilities = currentState.suggestedAbilities,
            ))
            if (!currentState.isSearching) {
                onSearchTextChange("")
            }
        }
    }

    private fun getSuggestedAbilities(searchText: String): List<PokeAbility> {
        if (searchText.isBlank()) {
            emptyList<PokeAbility>()
        }
        return allAbilities.filter { ability ->
            ability.name.uppercase().contains(searchText.trim().uppercase())
        }
    }
}