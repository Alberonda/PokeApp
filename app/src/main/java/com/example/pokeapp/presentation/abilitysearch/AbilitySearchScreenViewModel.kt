package com.example.pokeapp.presentation.abilitysearch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.di.DefaultDispatcher
import com.example.pokeapp.presentation.abilitysearch.entity.AbilitySearchScreenUiData
import com.example.pokeapp.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AbilitySearchScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    //private val getTypeDetailsUseCase: GetTypeDetailsUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState:
            MutableStateFlow<UiState<AbilitySearchScreenUiData>> = MutableStateFlow(
                UiState.Success(AbilitySearchScreenUiData())
            )
    val uiState: StateFlow<UiState<AbilitySearchScreenUiData>> = _uiState.asStateFlow()


    private val _cachedAbilities = listOf("aaa", "bbb")
    val cachedAbilities = _cachedAbilities

    private val _abilitiesList = MutableStateFlow(cachedAbilities)
    val abilitiesList = _abilitiesList.value

    fun onSearchTextChange(text: String) {
        (uiState.value as? UiState.Success)?.let { currentState ->
            _uiState.value = UiState.Success(AbilitySearchScreenUiData(
                isSearching = currentState.data.isSearching,
                searchText = text,
                abilitiesList = abilitiesList,
            ))
        }
    }

    fun onToggleSearch(selectedSearch: Boolean) {
        (uiState.value as? UiState.Success)?.data?.let { currentState ->
            _uiState.value = UiState.Success(AbilitySearchScreenUiData(
                isSearching = selectedSearch,
                searchText = currentState.searchText,
                abilitiesList = currentState.abilitiesList,
            ))
            if (!currentState.isSearching) {
                onSearchTextChange("")
            }
        }
    }
}