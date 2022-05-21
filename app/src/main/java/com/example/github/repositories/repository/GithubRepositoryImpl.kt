package com.example.github.repositories.repository

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.common.Result
import com.example.github.repositories.common.makeApiCall
import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.data.Response
import com.example.github.repositories.data.QUERY
import com.example.github.repositories.data.SORT
import com.example.github.repositories.data.ORDER
import com.example.github.repositories.data.UserDTO
import com.example.github.repositories.data.RepositoryDTO

class GithubRepositoryImpl(private val apiService: GitHubEndpoints) : IGithubRepository {

    override suspend fun searchRepositories(): Result<IFailure, Response> {
        return makeApiCall(
            apiCall = { apiService.searchRepositories(QUERY, SORT, ORDER) },
            successTransform = { it }
        )
    }

    override suspend fun getUser(userName: String): Result<IFailure, UserDTO> {
        return makeApiCall(
            apiCall = { apiService.getUser(userName) },
            successTransform = { it }
        )
    }

    override suspend fun getUserRepositories(userRepoUrl: String): Result<IFailure, List<RepositoryDTO>> {
        return makeApiCall(
            apiCall = { apiService.getUserRepositories(userRepoUrl) },
            successTransform = { it }
        )
    }
}