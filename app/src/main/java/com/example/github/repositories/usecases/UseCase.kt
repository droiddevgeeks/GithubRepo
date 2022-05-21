package com.example.github.repositories.usecases

import com.example.github.repositories.common.IFailure
import com.example.github.repositories.common.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class UseCase<in Params, out Type> where Type : Any? {
    abstract suspend fun run(params: Params): Result<IFailure, Type>

    operator fun invoke(
        scope: CoroutineScope,
        params: Params,
        onSuccess: (Type) -> Unit = {},
        onFailure: (IFailure) -> Unit = {}
    ) {
        val job = scope.async { run(params) }
        scope.launch {
            job.await().result(onFailure, onSuccess)
        }
    }
}