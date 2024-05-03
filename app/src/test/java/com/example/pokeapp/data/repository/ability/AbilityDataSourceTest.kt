package com.example.pokeapp.data.repository.ability

import com.example.pokeapp.base.EMPTY
import com.example.pokeapp.data.source.local.PokeAbilityLocalDataSource
import com.example.pokeapp.data.source.local.PokeAppDatabase
import com.example.pokeapp.data.source.local.entity.LocalAbilityDetailsResponse
import com.example.pokeapp.data.source.local.entity.LocalAllAbilitiesResponse
import com.example.pokeapp.data.source.network.PokeAbilityNetworkDataSource
import com.example.pokeapp.data.source.network.entity.ApiEffectEntry
import com.example.pokeapp.data.source.network.entity.ApiLanguage
import com.example.pokeapp.data.source.network.entity.ApiNameEntry
import com.example.pokeapp.data.source.network.entity.ApiPokeAbility
import com.example.pokeapp.data.source.network.entity.NetworkAbilityDetailsResponse
import com.example.pokeapp.data.source.network.entity.NetworkAllAbilitiesResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

class AbilityDataSourceTest {
    private lateinit var repository: AbilityDataSource
    private val networkDataSource: PokeAbilityNetworkDataSource = mockk()
    private val localDataSource: PokeAbilityLocalDataSource = mockk()

    @Before
    fun setUp() {
        repository = AbilityDataSource(
            networkDataSource,
            localDataSource
        )
    }

    @Test
    fun `getAbilityDetails when entry is cached`() = runTest {
        // Given
        val abilityToSearch = "ability 1"

        val localDataSourceResponse = getMockedLocalAbilityDetailsResponse()

        coEvery { localDataSource.getDetails(abilityToSearch) } coAnswers {
            flow {
                emit(localDataSourceResponse)
            }
        }

        val expectedResult = localDataSourceResponse.value.toDomainEntity()

        // When
        val result = repository.getAbilityDetails(abilityToSearch)

        // Then
        coVerify(exactly = 0) {
            networkDataSource.getAbilityData(abilityToSearch)
        }
        coVerify {
            localDataSource.getDetails(abilityToSearch)
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `getAbilityDetails when entry is missing`() = runTest {
        // Given
        val abilityToSearch = "ability 1"

        val remoteDataSourceResponse = getMockedNetworkAbilityDetailsResponse()

        coEvery { localDataSource.getDetails(abilityToSearch) } coAnswers {
            flow {
                emit(null)
            }
        }

        coEvery { localDataSource.insertDetails(any()) } returns Unit

        coEvery { networkDataSource.getAbilityData(any()) } answers {
            remoteDataSourceResponse
        }

        val expectedResult = remoteDataSourceResponse.toDomainEntity()

        // When
        val result = repository.getAbilityDetails(abilityToSearch)

        // Then
        coVerify {
            localDataSource.getDetails(abilityToSearch)
            localDataSource.insertDetails(any())
            networkDataSource.getAbilityData(abilityToSearch)
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `getAbilityDetails when local cache is expired`() = runTest {
        // Given
        val abilityToSearch = "ability 1"

        val remoteDataSourceResponse = getMockedNetworkAbilityDetailsResponse()

        coEvery { localDataSource.getDetails(abilityToSearch) } coAnswers {
            flow {
                emit(getMockedLocalAbilityDetailsResponse(expired = true))
            }
        }

        coEvery { localDataSource.insertDetails(any()) } returns Unit

        coEvery { networkDataSource.getAbilityData(any()) } answers {
            remoteDataSourceResponse
        }

        val expectedResult = remoteDataSourceResponse.toDomainEntity()

        // When
        val result = repository.getAbilityDetails(abilityToSearch)

        // Then
        coVerify {
            localDataSource.getDetails(abilityToSearch)
            localDataSource.insertDetails(any())
            networkDataSource.getAbilityData(abilityToSearch)
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `getAllAbilities when entry is cached`() = runTest {
        // Given
        val localDataSourceResponse = getMockedLocalAllAbilitiesResponse()

        coEvery { localDataSource.getAll() } coAnswers {
            flow {
                emit(localDataSourceResponse)
            }
        }

        val expectedResult = localDataSourceResponse.value.toDomainEntity()

        // When
        val result = repository.getAllAbilities()

        // Then
        coVerify(exactly = 0) {
            networkDataSource.getAllAbilities()
        }
        coVerify {
            localDataSource.getAll()
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `getAllAbilities when entry is missing`() = runTest {
        // Given
        val remoteDataSourceResponse = getMockedNetworkAllAbilitiesResponse()

        coEvery { localDataSource.getAll() } coAnswers {
            flow {
                emit(null)
            }
        }

        coEvery { localDataSource.insertAll(any()) } returns Unit

        coEvery { networkDataSource.getAllAbilities() } answers {
            remoteDataSourceResponse
        }

        val expectedResult = remoteDataSourceResponse.toDomainEntity()

        // When
        val result = repository.getAllAbilities()

        // Then
        coVerify {
            localDataSource.getAll()
            localDataSource.insertAll(any())
            networkDataSource.getAllAbilities()
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `getAllAbilities when local cache is expired`() = runTest {
        // Given
        val remoteDataSourceResponse = getMockedNetworkAllAbilitiesResponse()

        coEvery { localDataSource.getAll() } coAnswers {
            flow {
                emit(getMockedLocalAllAbilitiesResponse(expired = true))
            }
        }

        coEvery { localDataSource.insertAll(any()) } returns Unit

        coEvery { networkDataSource.getAllAbilities() } answers {
            remoteDataSourceResponse
        }

        val expectedResult = remoteDataSourceResponse.toDomainEntity()

        // When
        val result = repository.getAllAbilities()

        // Then
        coVerify {
            localDataSource.getAll()
            localDataSource.insertAll(any())
            networkDataSource.getAllAbilities()
        }
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    private fun getMockedLocalAllAbilitiesResponse(expired: Boolean = false) =
        LocalAllAbilitiesResponse(
            insertionDate = if (expired) getExpiredDate() else Date(),
            value = getMockedNetworkAllAbilitiesResponse()
        )

    private fun getMockedNetworkAllAbilitiesResponse() =
        NetworkAllAbilitiesResponse(
            results = listOf(
                ApiPokeAbility(name = "ability 1"),
                ApiPokeAbility(name = "ability 2")
            )
        )

    private fun getExpiredDate(): Date =
        Date.from(
            Instant.now().minus(
            PokeAppDatabase.CACHE_TIME_MINUTES.toLong(),
            ChronoUnit.MINUTES)
        )

    private fun getMockedLocalAbilityDetailsResponse(expired: Boolean = false) =
        LocalAbilityDetailsResponse(
            abilityName = "mockedAbilityName",
            insertionDate = if (expired) getExpiredDate() else Date(),
            value = getMockedNetworkAbilityDetailsResponse()
        )

    private fun getMockedNetworkAbilityDetailsResponse() =
        NetworkAbilityDetailsResponse(
            name = "mockedAbilityName",
            entries = listOf(
                ApiEffectEntry(
                    language = ApiLanguage(
                        code = "de",
                        url = String.EMPTY
                    ),
                    effect = "Effect text"
                ),
                ApiEffectEntry(
                    language = ApiLanguage(
                        code = "en",
                        url = String.EMPTY
                    ),
                    effect = "Effect text"
                )
            ),
            otherNames = listOf(
                ApiNameEntry(
                    language = ApiLanguage(
                        code = "es",
                        url = String.EMPTY
                    ),
                    name = "ability name"
                ),
                ApiNameEntry(
                    language = ApiLanguage(
                        code = "de",
                        url = String.EMPTY
                    ),
                    name = "ability name"
                )
            )
        )
}