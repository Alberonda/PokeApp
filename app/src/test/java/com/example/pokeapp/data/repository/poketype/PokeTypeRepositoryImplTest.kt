package com.example.pokeapp.data.repository.poketype

import com.example.pokeapp.base.Constants
import com.example.pokeapp.base.mergeByKeys
import com.example.pokeapp.data.source.network.PokeTypeNetworkDataSource
import com.example.pokeapp.data.source.network.entity.AllTypesResponse
import com.example.pokeapp.data.source.network.entity.ApiPokeType
import com.example.pokeapp.data.source.network.entity.ApiTypeRelations
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PokeTypeRepositoryImplTest {

    private lateinit var repository: PokeTypeRepositoryImpl
    private val networkDataSource: PokeTypeNetworkDataSource = mockk()

    @Before
    fun setUp() {
        repository = PokeTypeRepositoryImpl(networkDataSource)
    }

    @Test
    fun getTypeDetails() = runTest {
        // Given
        val typesToSearch = listOf("type1", "type2")
        val serverResponse = getMockedTypeDetailsResponse()

        coEvery { networkDataSource.getTypeData(any()) } returns serverResponse

        val mappedResponses = listOf(
            serverResponse,
            serverResponse
        ).map {
            PokeTypeDetails.fromDataEntity(it)
        }

        val expectedResult = PokeTypeDetails(
            mappedResponses.joinToString(
                Constants.TYPES_NAMES_SEPARATOR
            ) { it.name },
            mappedResponses.flatMap {
                it.attackerTypesRelation
            }.mergeByKeys { a: Double, b: Double -> a * b },
            mappedResponses.flatMap {
                it.defenderTypesRelation
            }.mergeByKeys { a: Double, b: Double -> a * b }
        )

        // When
        val result = repository.getTypesDetails(typesToSearch)

        // Then
        coVerify {
            networkDataSource.getTypeData("type1")
            networkDataSource.getTypeData("type2")
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun getAllTypes() = runTest {
        // Given
        val serverResponse = getMockedAllTypesResponse()
        coEvery { networkDataSource.getAllTypes() } returns serverResponse

        val expectedResult = serverResponse.results.map {
            PokeType.fromDataEntity(it)
        }

        // When
        val result = repository.getAllTypes()

        // Then
        Assert.assertEquals(
            result,
            expectedResult
        )
    }

    private fun getMockedAllTypesResponse() =
        AllTypesResponse(
            listOf(
                ApiPokeType(
                    "type 1"
                ),
                ApiPokeType(
                    "type 2"
                ),
                ApiPokeType(
                    "type 3"
                ),
                ApiPokeType(
                    "type 4"
                )
            )
        )

    private fun getMockedTypeDetailsResponse() =
        TypeDetailsResponse(
            "Mocked name",
            ApiTypeRelations(
                listOf(
                    ApiPokeType("a1"),
                    ApiPokeType("a2")
                ),
                listOf(
                    ApiPokeType("b1")
                ),
                listOf(
                    ApiPokeType("c1"),
                    ApiPokeType("c2"),
                    ApiPokeType("c3")
                ),
                listOf(
                    ApiPokeType("d1"),
                    ApiPokeType("d2")
                ),
                listOf(
                    ApiPokeType("e1"),
                ),
                listOf(
                    ApiPokeType("f1")
                )
            )
        )
}