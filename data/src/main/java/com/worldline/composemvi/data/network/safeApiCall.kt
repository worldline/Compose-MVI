package com.worldline.composemvi.data.network

import com.worldline.composemvi.domain.error.AppError
import com.worldline.composemvi.domain.error.mapToAppError
import com.worldline.composemvi.domain.model.StatusCode
import com.worldline.composemvi.domain.model.usecase.DataResult
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Safe api call function that wraps api calls with a try-catch block and handles network errors.
 *
 * The function takes a lambda function that makes the api call and another lambda function that
 * handles the error. The error handling lambda function is optional and by default it returns null.
 *
 * @param D The data to be returned by the api call in case of success.
 * @param E The error type to be returned in case of error. This error extends [DataError].
 * @param apiCall The lambda function that makes the api call.
 * @param onError The lambda function that handles the error.
 * @return [DataResult] that holds the data or error.
 *
 * @see [DataResult]
 * @see [Error]
 */
suspend fun <D, E> safeApiCall(
    apiCall: suspend () -> D,
    onError: suspend (StatusCode) -> E? = { null }
): DataResult<D, E> {
    return withContext(Dispatchers.IO) {
        try {
            DataResult.Success(apiCall.invoke())
        } catch (e: IOException) {
            when (val appError = e.mapToAppError()) {
                is AppError.NetworkError -> when (val error = onError(appError.statusCode)) {
                    null -> throw e
                    else -> DataResult.Error(error)
                }

                is AppError.CommonError,
                is AppError.TextualError -> throw e
            }
        }
    }
}
