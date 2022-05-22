package com.example.github.repositories.repository

import com.example.github.repositories.TestHelper
import com.example.github.repositories.common.IFailure
import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.Response
import com.example.github.repositories.data.UserDTO
import com.nhaarman.mockitokotlin2.whenever
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.doThrow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
@DisplayName("Github Repository Test Cases")
class GithubRepositoryTest {

    private lateinit var repo: IGithubRepository
    private val apiService by lazy { mock<GitHubEndpoints>() }

    @BeforeEach
    fun setUp() {
        repo = GithubRepositoryImpl(apiService)
    }

    @Test
    @DisplayName("Testing SearchRepositories success case")
    fun `should fetch search repositories successfully`() {
        val response = mock<Response>()
        runTest {
            whenever(apiService.searchRepositories(any(), any(), any())).thenReturn(response)
            val actualResult = repo.searchRepositories()

            assertEquals(response, actualResult.successValue())
            verify(apiService, times(1)).searchRepositories(any(), any(), any())
            verifyNoMoreInteractions(apiService)
        }
    }

    @Test
    @DisplayName("Testing SearchRepositories failed case")
    fun `should give error when  search repositories failed`() {
        val exception = TestHelper.getApiError<Unit>(TestHelper.NORMAL_ERROR_CODE)
        runTest {
            whenever(apiService.searchRepositories(any(), any(), any())).doThrow(exception)
            val actualResult = repo.searchRepositories()

            assertEquals(exception.code(), TestHelper.NORMAL_ERROR_CODE)
            assertTrue(actualResult.errorValue() is IFailure)
            verify(apiService, times(1)).searchRepositories(any(), any(), any())
            verifyNoMoreInteractions(apiService)
        }
    }

    @Test
    @DisplayName("Testing GetUser success case")
    fun `should fetch GetUser data successfully`() {
        val response = mock<UserDTO>()
        runTest {
            whenever(apiService.getUser(any())).thenReturn(response)
            val actualResult = repo.getUser("")

            assertEquals(response, actualResult.successValue())
            verify(apiService, times(1)).getUser(any())
            verifyNoMoreInteractions(apiService)
        }
    }

    @Test
    @DisplayName("Testing GetUser failed case")
    fun `should give error when  get User api failed`() {
        val exception = TestHelper.getApiError<Unit>(TestHelper.NORMAL_ERROR_CODE)
        runTest {
            whenever(apiService.getUser(any())).doThrow(exception)
            val actualResult = repo.getUser("")

            assertEquals(exception.code(), TestHelper.NORMAL_ERROR_CODE)
            assertTrue(actualResult.errorValue() is IFailure)
            verify(apiService, times(1)).getUser(any())
            verifyNoMoreInteractions(apiService)
        }
    }

    @Test
    @DisplayName("Testing GetUserRepositories success case")
    fun `should fetch UserRepositories successfully`() {
        val response = mock<List<RepositoryDTO>>()
        runTest {
            whenever(apiService.getUserRepositories(any())).thenReturn(response)
            val actualResult = repo.getUserRepositories("")

            assertEquals(response, actualResult.successValue())
            verify(apiService, times(1)).getUserRepositories(any())
            verifyNoMoreInteractions(apiService)
        }
    }

    @Test
    @DisplayName("Testing GetUserRepositories failed case")
    fun `should give error when  GetUserRepositories api failed`() {
        val exception = TestHelper.getApiError<Unit>(TestHelper.NORMAL_ERROR_CODE)
        runTest {
            whenever(apiService.getUserRepositories(any())).doThrow(exception)
            val actualResult = repo.getUserRepositories("")

            assertEquals(exception.code(), TestHelper.NORMAL_ERROR_CODE)
            assertTrue(actualResult.errorValue() is IFailure)
            verify(apiService, times(1)).getUserRepositories(any())
            verifyNoMoreInteractions(apiService)
        }
    }
}