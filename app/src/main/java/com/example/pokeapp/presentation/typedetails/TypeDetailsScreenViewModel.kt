package com.example.pokeapp.presentation.typedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    ).split(NAMES_SEPARATOR).filterNot { it.isBlank() }

    private val searchedTypesAsUiData
        get() = PokeTypeUiData.mapDomainToUiEntities(
            typesToSearch.map { PokeType(it) }
        )

    init {
        getTypeDetails(typesToSearch)
    }

    companion object {
        const val NAMES_SEPARATOR = ", "
    }

    private fun getTypeDetails(typesToSearch: List<String>) {
        _uiState.value = TypeDetailsScreenUiState(isLoading = true, throwError = false)
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                val deferredResults = typesToSearch.map {
                    async { getTypeDetailsUseCase.execute(it) }
                }
                awaitAll(*deferredResults.toTypedArray())
            }.run {
                val mappedResult = this.filter { it.isSuccess }.mapNotNull {
                    it.getOrNull()
                }

                if (mappedResult.isNotEmpty()) {
                    _uiState.value = TypeDetailsScreenUiState(
                        typesDetails = mapDomainToUiEntities(
                            PokeTypeDetails(
                                name = mappedResult.joinToString(
                                    NAMES_SEPARATOR
                                ) { it.name },
                                attackerTypesRelation = mappedResult.flatMap { it.attackerTypesRelation },
                                defenderTypesRelation = mappedResult.flatMap { it.defenderTypesRelation }
                            )
                        )
                    )
                } else {
                    _uiState.value = TypeDetailsScreenUiState(throwError = true)
                }
            }
        }
    }

    private fun mapDomainToUiEntities(typeDetails: PokeTypeDetails): TypeDetailsScreenUiData {

        val groupedRelations = typeDetails.defenderTypesRelation.groupBy {
            it.first
        }.mapValues { (_, relations) ->
            relations.reduce { acc, next ->
                Pair(
                    acc.first,
                    acc.second * next.second
                )
            }
        }.values.mapNotNull { relationPair ->
            PokeTypeUiData.mapDomainToUiResources(relationPair.first)?.let { typeResources ->
                Pair(
                    PokeTypeUiData(typeResources),
                    relationPair.second
                )
            }
        }

        return TypeDetailsScreenUiData(
            searchedTypes = searchedTypesAsUiData,
            veryEffectiveRelations = groupedRelations.filter { it.second == QUAD_DAMAGE_FACTOR }
                .map { it.first },
            effectiveRelations = groupedRelations.filter { it.second == DOUBLE_DAMAGE_FACTOR }
                .map { it.first },
            notEffectiveRelations = groupedRelations.filter { it.second == HALF_DAMAGE_FACTOR }
                .map { it.first },
            veryNotEffectiveRelations = groupedRelations.filter { it.second == QUARTER_DAMAGE_FACTOR }
                .map { it.first },
            unaffectedRelations = groupedRelations.filter { it.second == NO_DAMAGE_FACTOR }
                .map { it.first },
        )
    }
}