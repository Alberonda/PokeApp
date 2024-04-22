package com.example.pokeapp.data.repository.poketype

import com.example.pokeapp.data.source.network.PokeTypeNetworkDataSource
import com.example.pokeapp.data.source.network.entity.AllTypesResponse
import com.example.pokeapp.data.source.network.entity.ApiPokeType
import com.example.pokeapp.data.source.network.entity.ApiTypeRelations
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import io.mockk.coEvery
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
        val typeNameToSearch = "example name"
        val serverResponse = getMockedTypeDetailsResponse()

        coEvery { networkDataSource.getTypeData(typeNameToSearch) } returns serverResponse

        val expectedResult = PokeTypeDetails.fromDataEntity(
            serverResponse
        )

        // When
        val result = repository.getTypeDetails(typeNameToSearch)

        // Then
        Assert.assertEquals(
            result,
            expectedResult
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