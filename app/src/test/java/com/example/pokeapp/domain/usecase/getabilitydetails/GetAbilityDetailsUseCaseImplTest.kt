package com.example.pokeapp.domain.usecase.getabilitydetails

import com.example.pokeapp.data.repository.ability.AbilityRepository
import com.example.pokeapp.domain.entity.PokeAbility
import com.example.pokeapp.domain.usecase.getallabilities.GetAbilityDetailsUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetAbilityDetailsUseCaseImplTest {

    private lateinit var useCase: GetAbilityDetailsUseCaseImpl
    private val abilityRepository: AbilityRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetAbilityDetailsUseCaseImpl(abilityRepository)
    }

    @Test
    fun `execute with success response from repository`() = runTest {
        // Given
        val abilityToSearch = "ability 1"
        val repositoryResponse = getMockedAbilityDetailResponse()

        coEvery { abilityRepository.getAbilityDetails(abilityToSearch) } returns repositoryResponse

        val expectedResult = Result.success(repositoryResponse)

        // When
        val result = useCase.execute(abilityToSearch)

        // Then
        Assert.assertEquals(
            expectedResult,
            result
        )
    }

    @Test
    fun `execute with failure response from repository`() = runTest {
        // Given
        val abilityToSearch = "ability 1"
        val serverException = Exception("Server exception")

        coEvery { abilityRepository.getAbilityDetails(abilityToSearch) } throws serverException

        val expectedResult = Result.failure<Exception>(
            serverException
        )

        // When
        val result = useCase.execute(abilityToSearch)

        // Then
        Assert.assertEquals(
            result,
            expectedResult
        )
    }

    private fun getMockedAbilityDetailResponse() =
        PokeAbility(
            name = "mockedName",
            description = "description mocked"
        )
}