package com.example.pokeapp.presentation.typeslanding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.di.DefaultDispatcher
import com.example.pokeapp.domain.usecase.getalltypes.GetAllTypesUseCase
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData
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
class TypesLandingScreenViewModel @Inject constructor(
    private val getAllTypesUseCase: GetAllTypesUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState:
            MutableStateFlow<UiState<TypesLandingScreenUiData>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<TypesLandingScreenUiData>> = _uiState.asStateFlow()

    init {
        getAllTypesData()
    }

    fun getAllTypesData() {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                withContext(defaultDispatcher) {
                    getAllTypesUseCase.execute()
                }.run {
                    if (this.isSuccess) {
                        _uiState.value =
                            UiState.Success(
                                TypesLandingScreenUiData(
                                    PokeTypeUiData.mapDomainToUiEntities(
                                        this.getOrDefault(emptyList())
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