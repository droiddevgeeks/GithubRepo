package com.example.github.repositories

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class TestHelper {

    companion object {
        internal const val NORMAL_ERROR_CODE = 400
        internal inline fun <reified T> getApiError(errorCode: Int): HttpException {
            val error = Response.error<T>(
                errorCode,
                ("{\"status\":\"failed\", \"title\":\"setting api\",\"display_message\":\"DEFAULT_ERROR_MSG\",\"data\":\"null\"}")
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            return HttpException(error)
        }
    }
}
