package com.example.pokeapp.domain.usecase.gettypedetails

import com.example.pokeapp.data.repository.poketype.PokeTypeRepository
import com.example.pokeapp.domain.entity.PokeType
import com.example.pokeapp.domain.entity.PokeTypeDetails
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetTypeDetailsUseCaseImplTest {

    private lateinit var useCase: GetTypeDetailsUseCaseImpl
    private val pokeTypeRepository: PokeTypeRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetTypeDetailsUseCaseImpl(pokeTypeRepository)
    }

    @Test
    fun `execute with success response from repository`() = runTest {
        // Given
        val typesToSearch = listOf("type1", "type2")
        val serverResponse = getMockedPokeTypeDetailsResponse()

        coEvery { pokeTypeRepository.getTypesDetails(typesToSearch) } returns serverResponse

        val expectedResult = Result.success(
            serverResponse
        )

        // When
        val result = useCase.execute(typesToSearch)

        // Then
        Assert.assertEquals(
            result,
            expectedResult
        )
    }

    @Test
    fun `execute with failure response from repository`() = runTest {
        // Given
        val typesToSearch = listOf("type1", "type2")
        val serverException = Exception("Server exception")

        coEvery { pokeTypeRepository.getTypesDetails(typesToSearch) } throws serverException

        val expectedResult = Result.failure<Exception>(
            serverException
        )

        // When
        val result = useCase.execute(typesToSearch)

        // Then
        Assert.assertEquals(
            result,
            expectedResult
        )
    }

    private fun getMockedPokeTypeDetailsResponse() =
        PokeTypeDetails(
            defenderTypesRelation = listOf(
                Pair(
                    PokeType("type 1"),
                    2.0
                )
            ),
            attackerTypesRelation = listOf(
                Pair(
                    PokeType("type 2"),
                    0.5
                )
            )
        )
}