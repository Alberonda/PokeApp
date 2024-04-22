package com.example.pokeapp.domain.usecase.getalltypes

import com.example.pokeapp.data.repository.poketype.PokeTypeRepository
import com.example.pokeapp.domain.entity.PokeType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class GetAllTypesUseCaseImplTest {

    private lateinit var useCase: GetAllTypesUseCaseImpl
    private val pokeTypeRepository: PokeTypeRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetAllTypesUseCaseImpl(pokeTypeRepository)
    }

    @Test
    fun `execute with success response from repository`() = runTest {
        // Given
        val serverResponse = getMockedAllTypesResponse()
        val maxValidTypes = 18

        coEvery { pokeTypeRepository.getAllTypes() } returns serverResponse

        val expectedResult = Result.success(
            serverResponse.take(maxValidTypes)
        )

        // When
        val result = useCase.execute()

        // Then
        Assert.assertEquals(
            result,
            expectedResult
        )
    }

    @Test
    fun `execute with failure response from repository`() = runTest {
        // Given
        val serverException = Exception("Server exception")

        coEvery { pokeTypeRepository.getAllTypes() } throws serverException

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

    private fun getMockedAllTypesResponse() =
        List(20) { index -> PokeType("Type $index") }
}