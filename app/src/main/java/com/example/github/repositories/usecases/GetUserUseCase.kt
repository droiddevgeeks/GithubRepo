package com.example.github.repositories.usecases

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.common.Result
import com.example.github.repositories.data.UserDTO
import com.example.github.repositories.repository.IGithubRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repo: IGithubRepository
) : UseCase<String, UserDTO>() {

    override suspend fun run(params: String): Result<IFailure, UserDTO> {
        return repo.getUser(params)
    }
}