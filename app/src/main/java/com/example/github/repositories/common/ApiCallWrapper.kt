package com.example.github.repositories.common

import com.example.github.repositories.data.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

suspend inline fun <T, X> makeApiCall(
    crossinline apiCall: suspend () -> T,
    successTransform: (T) -> X,
): Result<Failure, X> {
    return try {
        val response = withContext(Dispatchers.IO) { apiCall.invoke() }
        if (response is Response<*>) {
            if (response.code() >= 400) {
                val errorMsg = response.errorBody().toString()
                Result.Error(
                    Failure.DisplayableError(ErrorModel(errorMsg, response.code().toString()))
                )
            } else Result.Success(successTransform(response))
        } else {
            Result.Success(successTransform(response))
        }
    } catch (e: Exception) {
        return when (e) {
            is HttpException -> {
                Result.Error(Failure.HttpError(ErrorModel("SomeThing went wrong", "")))
            }
            else -> {
                Result.Error(Failure.DisplayableError(ErrorModel("Please try again", "")))
            }
        }
    }
}