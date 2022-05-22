package com.example.github.repositories.usecase

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.repository.IGithubRepository
import com.example.github.repositories.usecases.GetUserRepositoryUseCase
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import com.example.github.repositories.common.Result
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue


@ExperimentalCoroutinesApi
@DisplayName("Get User Repository UseCase Test")
class GetUserRepositoryUseCaseTest {

    private lateinit var useCase: GetUserRepositoryUseCase
    private val repo by lazy { mock<IGithubRepository>() }

    @BeforeEach
    fun setUp() {
        useCase = GetUserRepositoryUseCase(repo)
    }

    @Test
    @DisplayName("Testing Get UserRepository UseCase success case")
    fun `should give success for UserRepository Api`() {
        val data = mock<List<RepositoryDTO>>()
        runTest {
            whenever(repo.getUserRepositories(any())).thenReturn(Result.Success(data))

            val actualResult = useCase.run("")

            assertEquals(data, actualResult.successValue())
            verify(repo, times(1)).getUserRepositories(any())
            verifyNoMoreInteractions(repo)
        }
    }

    @Test
    @DisplayName("Testing Get UserRepository fail case")
    fun `should give error userRepository api is called`() {
        val error = mock<IFailure>()
        runTest {
            whenever(repo.getUserRepositories(any())).thenReturn(Result.Error(error))

            val actualResult = useCase.run("")

            assertTrue(actualResult.errorValue() is IFailure)
            assertEquals(error, actualResult.errorValue())
            verify(repo, times(1)).getUserRepositories(any())
            verifyNoMoreInteractions(repo)
        }
    }
}