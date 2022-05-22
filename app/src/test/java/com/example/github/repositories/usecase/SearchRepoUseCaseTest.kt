package com.example.github.repositories.usecase

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.common.Result
import com.example.github.repositories.data.Response
import com.example.github.repositories.repository.IGithubRepository
import com.example.github.repositories.usecases.SearchRepositoryUseCase
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
@DisplayName("Search Repository UseCase Test")
class SearchRepoUseCaseTest {

    private lateinit var useCase: SearchRepositoryUseCase
    private val repo by lazy { mock<IGithubRepository>() }

    @BeforeEach
    fun setUp() {
        useCase = SearchRepositoryUseCase(repo)
    }

    @Test
    @DisplayName("Testing Search Repositories api success case")
    fun `should give success for searchRepositories Api`() {
        val data = mock<Response>()
        runTest {
            whenever(repo.searchRepositories()).thenReturn(Result.Success(data))

            val actualResult = useCase.run(Unit)

            assertEquals(data, actualResult.successValue())
            verify(repo, times(1)).searchRepositories()
            verifyNoMoreInteractions(repo)
        }
    }

    @Test
    @DisplayName("Testing Search Repositories api fail case")
    fun `should give error when user api is called`() {
        val error = mock<IFailure>()
        runTest {
            whenever(repo.searchRepositories()).thenReturn(Result.Error(error))

            val actualResult = useCase.run(Unit)

            assertTrue(actualResult.errorValue() is IFailure)
            assertEquals(error, actualResult.errorValue())
            verify(repo, times(1)).searchRepositories()
            verifyNoMoreInteractions(repo)
        }
    }
}