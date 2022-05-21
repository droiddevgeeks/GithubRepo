package com.example.github.repositories.repository

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.common.Result
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.Response
import com.example.github.repositories.data.UserDTO

interface IGithubRepository {
    suspend fun searchRepositories(): Result<IFailure, Response>
    suspend fun getUser(userName: String): Result<IFailure, UserDTO>
    suspend fun getUserRepositories(userRepoUrl: String): Result<IFailure, List<RepositoryDTO>>
}