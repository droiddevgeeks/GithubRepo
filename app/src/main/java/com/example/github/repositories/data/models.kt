package com.example.github.repositories.data

import com.example.github.repositories.common.ErrorModel
import com.example.github.repositories.common.IFailure

sealed class ApiState<out T> {
    data class Loading(val isLoading: Boolean) : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Failure(val failure: IFailure) : ApiState<Nothing>()
}

sealed class Failure(override val errorModel: ErrorModel) : IFailure {
    class HttpError(error: ErrorModel) : Failure(error)
    class DisplayableError(error: ErrorModel) : Failure(error)
}