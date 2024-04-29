package com.example.pokeapp.data.repository.poketype

import com.example.pokeapp.base.Constants
import com.example.pokeapp.base.mergeByKeys
import com.example.pokeapp.data.source.local.PokeTypeDatabase
import com.example.pokeapp.data.source.local.PokeTypeLocalDataSource
import com.example.pokeapp.data.source.network.PokeTypeNetworkDataSource
import com.example.pokeapp.data.source.network.entity.AllTypesResponse
import com.example.pokeapp.data.source.network.entity.ApiPokeType
import com.example.pokeapp.data.source.network.entity.ApiTypeRelations
import com.example.pokeapp.data.source.network.entity.TypeDetailsResponse
import com.example.pokeapp.data.source.local.entity.PokeType as LocalPokeType
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class PokeTypeDataSourceTest {

    private lateinit var repository: PokeTypeDataSource
    private val networkDataSource: PokeTypeNetworkDataSource = mockk()
    private val localDataSource: PokeTypeLocalDataSource = mockk()

    @Before
    fun setUp() {
        repository = PokeTypeDataSource(
            networkDataSource,
            localDataSource
        )
    }

    @Test
    fun `getTypeDetails when entry is cached`() = runTest {
        // Given
        val typesToSearch = listOf("type1")

        val localDataSourceResponse = getMockedLocalTypeDetailsResponse("type1")

        coEvery { localDataSource.getPokeTypeByName(any()) } coAnswers {
            flow {
                emit(getMockedLocalTypeDetailsResponse(firstArg<String>()))
            }
        }

        val mappedResponses = listOf(
            localDataSourceResponse
        ).map {
            it.toDomainEntity()
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
        coVerify(exactly = 0) {
            networkDataSource.getTypeData("type1")
        }
        coVerify {
            localDataSource.getPokeTypeByName("type1")
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `getTypeDetails when entry is missing`() = runTest {
        // Given
        val typesToSearch = listOf("type1")

        val remoteDataSourceResponse = getMockedNetworkTypeDetailsResponse("type1")

        coEvery { localDataSource.getPokeTypeByName(any()) } coAnswers {
            flow {
                emit(null)
            }
        }

        coEvery { localDataSource.insertPokeType(any()) } returns Unit

        coEvery { networkDataSource.getTypeData(any()) } answers {
            getMockedNetworkTypeDetailsResponse(firstArg<String>())
        }

        val mappedResponses = listOf(
            remoteDataSourceResponse
        ).map {
            it.toDomainEntity()
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
            localDataSource.getPokeTypeByName("type1")
            localDataSource.insertPokeType(any())
            networkDataSource.getTypeData("type1")
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `getTypeDetails when local cache is expired`() = runTest {
        // Given
        val typesToSearch = listOf("type1")

        val remoteDataSourceResponse = getMockedNetworkTypeDetailsResponse("type1")

        coEvery { localDataSource.getPokeTypeByName(any()) } coAnswers {
            flow {
                emit(getMockedLocalTypeDetailsResponse(firstArg<String>(), expired = true))
            }
        }

        coEvery { localDataSource.insertPokeType(any()) } returns Unit

        coEvery { networkDataSource.getTypeData(any()) } answers {
            getMockedNetworkTypeDetailsResponse(firstArg<String>())
        }

        val mappedResponses = listOf(
            remoteDataSourceResponse
        ).map {
            it.toDomainEntity()
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
            localDataSource.getPokeTypeByName("type1")
            localDataSource.insertPokeType(any())
            networkDataSource.getTypeData("type1")
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

    private fun getExpiredDate(): Date =
        Date.from(Instant.now().minus(
            PokeTypeDatabase.CACHE_TIME_MINUTES.toLong(),
            ChronoUnit.MINUTES)
        )

    private fun getMockedLocalTypeDetailsResponse(
        mockedName: String = "Mocked name",
        expired: Boolean = false
    ) =
        LocalPokeType(
            mockedName,
            if (expired) getExpiredDate() else Date(),
            listOf(
                LocalPokeType("a1"),
                LocalPokeType("a2")
            ),
            listOf(
                LocalPokeType("b1")
            ),
            listOf(
                LocalPokeType("c1"),
                LocalPokeType("c2"),
                LocalPokeType("c3")
            ),
            listOf(
                LocalPokeType("d1"),
                LocalPokeType("d2")
            ),
            listOf(
                LocalPokeType("e1"),
            ),
            listOf(
                LocalPokeType("f1")
            )
        )

    private fun getMockedNetworkTypeDetailsResponse(mockedName: String = "Mocked name") =
        TypeDetailsResponse(
            mockedName,
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