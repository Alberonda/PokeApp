package com.example.pokeapp.domain.usecase.getallabilities

import com.example.pokeapp.data.repository.ability.AbilityRepository
import com.example.pokeapp.domain.entity.PokeAbilityName
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAllAbilitiesUseCaseImplTest {

    private lateinit var useCase: GetAllAbilitiesUseCaseImpl
    private val abilityRepository: AbilityRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetAllAbilitiesUseCaseImpl(abilityRepository)
    }

    @Test
    fun `execute with success response from repository`() = runTest {
        // Given
        val repositoryResponse = getMockedAllAbilityResponse()

        coEvery { abilityRepository.getAllAbilities() } returns repositoryResponse

        val expectedResult = Result.success(repositoryResponse)

        // When
        val result = useCase.execute()

        // Then
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `execute with failure response from repository`() = runTest {
        // Given
        val serverException = Exception("Server exception")

        coEvery { abilityRepository.getAllAbilities() } throws serverException

        val expectedResult = Result.failure<Exception>(
            serverException
        )

        // When
        val result = useCase.execute()

        // Then
        Assert.assertEquals(
            result,
            expectedResult
        )
    }

    private fun getMockedAllAbilityResponse() =
        listOf(
            PokeAbilityName("ability 1"),
            PokeAbilityName("ability 2"),
        )
}