package com.example.github.repositories.usecase

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.common.Result
import com.example.github.repositories.data.UserDTO
import com.example.github.repositories.repository.IGithubRepository
import com.example.github.repositories.usecases.GetUserUseCase
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
@DisplayName("Get User UseCase Test")
class GetUserUseCaseTest {

    private lateinit var useCase: GetUserUseCase
    private val repo by lazy { mock<IGithubRepository>() }

    @BeforeEach
    fun setUp() {
        useCase = GetUserUseCase(repo)
    }

    @Test
    @DisplayName("Testing Get User api success case")
    fun `should give success for user Api`() {
        val data = mock<UserDTO>()
        runTest {
            whenever(repo.getUser(any())).thenReturn(Result.Success(data))

            val actualResult = useCase.run("")

            assertEquals(data, actualResult.successValue())
            verify(repo, times(1)).getUser(any())
            verifyNoMoreInteractions(repo)
        }
    }

    @Test
    @DisplayName("Testing Get User api fail case")
    fun `should give error when user api is called`() {
        val error = mock<IFailure>()
        runTest {
            whenever(repo.getUser(any())).thenReturn(Result.Error(error))

            val actualResult = useCase.run("")

            assertTrue(actualResult.errorValue() is IFailure)
            assertEquals(error, actualResult.errorValue())
            verify(repo, times(1)).getUser(any())
            verifyNoMoreInteractions(repo)
        }
    }
}