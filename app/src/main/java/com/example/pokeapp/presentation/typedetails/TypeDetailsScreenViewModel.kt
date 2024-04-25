package com.example.pokeapp.presentation.typedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.base.Constants.TYPES_NAMES_SEPARATOR
import com.example.pokeapp.di.DefaultDispatcher
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import com.example.pokeapp.domain.entity.PokeTypeDetails.Companion.DOUBLE_DAMAGE_FACTOR
import com.example.pokeapp.domain.entity.PokeTypeDetails.Companion.HALF_DAMAGE_FACTOR
import com.example.pokeapp.domain.entity.PokeTypeDetails.Companion.NO_DAMAGE_FACTOR
import com.example.pokeapp.domain.entity.PokeTypeDetails.Companion.QUAD_DAMAGE_FACTOR
import com.example.pokeapp.domain.entity.PokeTypeDetails.Companion.QUARTER_DAMAGE_FACTOR
import com.example.pokeapp.domain.usecase.gettypedetails.GetTypeDetailsUseCase
import com.example.pokeapp.presentation.typedetails.entity.TypeDetailsScreenUiData
import com.example.pokeapp.presentation.typeslanding.entity.PokeTypeUiData
import com.example.pokeapp.ui.navigation.TypeDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TypeDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTypeDetailsUseCase: GetTypeDetailsUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(TypeDetailsScreenUiState())
    val uiState: StateFlow<TypeDetailsScreenUiState> = _uiState.asStateFlow()

    private val typesToSearch: List<String> = checkNotNull<String>(
        savedStateHandle[TypeDetails.selectedTypeArg]
    ).split(TYPES_NAMES_SEPARATOR).filterNot { it.isBlank() }

    private val searchedTypesAsUiData
        get() = PokeTypeUiData.mapDomainToUiEntities(
            typesToSearch.map { PokeType(it) }
        )

    init {
        if (typesToSearch.isNotEmpty()){
            getTypeDetails(typesToSearch)
        } else {
            _uiState.value = TypeDetailsScreenUiState(throwError = true)
        }
    }

    private fun getTypeDetails(typesToSearch: List<String>) {
        _uiState.value = TypeDetailsScreenUiState(isLoading = true, throwError = false)
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                getTypeDetailsUseCase.execute(typesToSearch)
            }.run {
                _uiState.value = this.getOrNull()?.let {
                    TypeDetailsScreenUiState(
                        typesDetails = mapDomainToUiEntities(it)
                    )
                } ?: TypeDetailsScreenUiState(throwError = true)
            }
        }
    }

    private fun mapDomainToUiEntities(typeDetails: PokeTypeDetails): TypeDetailsScreenUiData {

        val uiResourcesRelations = typeDetails.defenderTypesRelation.mapNotNull { relationPair ->
            PokeTypeUiData.mapDomainToUiResources(relationPair.first)?.let { typeResources ->
                Pair(
                    PokeTypeUiData(typeResources),
                    relationPair.second
                )
            }
        }

        return TypeDetailsScreenUiData(
            searchedTypes = searchedTypesAsUiData,
            veryEffectiveRelations = uiResourcesRelations.filter { it.second == QUAD_DAMAGE_FACTOR }
                .map { it.first },
            effectiveRelations = uiResourcesRelations.filter { it.second == DOUBLE_DAMAGE_FACTOR }
                .map { it.first },
            notEffectiveRelations = uiResourcesRelations.filter { it.second == HALF_DAMAGE_FACTOR }
                .map { it.first },
            veryNotEffectiveRelations = uiResourcesRelations.filter { it.second == QUARTER_DAMAGE_FACTOR }
                .map { it.first },
            unaffectedRelations = uiResourcesRelations.filter { it.second == NO_DAMAGE_FACTOR }
                .map { it.first },
        )
    }
}