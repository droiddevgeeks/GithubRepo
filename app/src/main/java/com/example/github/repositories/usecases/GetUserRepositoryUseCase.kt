package com.example.github.repositories.usecases

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.common.Result
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.repository.IGithubRepository
import javax.inject.Inject

class GetUserRepositoryUseCase @Inject constructor(
    private val repo: IGithubRepository
) : UseCase<String, List<RepositoryDTO>>() {

    override suspend fun run(params: String): Result<IFailure, List<RepositoryDTO>> {
        return repo.getUserRepositories(params)
    }
}