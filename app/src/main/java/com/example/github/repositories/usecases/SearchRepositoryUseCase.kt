package com.example.github.repositories.usecases

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.common.Result
import com.example.github.repositories.data.Response
import com.example.github.repositories.repository.IGithubRepository
import javax.inject.Inject

class SearchRepositoryUseCase @Inject constructor(
    private val repo: IGithubRepository
) : UseCase<Unit, Response>() {

    override suspend fun run(params: Unit): Result<IFailure, Response> {
        return repo.searchRepositories()
    }
}