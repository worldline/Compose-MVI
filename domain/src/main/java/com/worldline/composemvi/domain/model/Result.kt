package com.worldline.composemvi.domain.model

import androidx.lifecycle.MutableLiveData
import com.worldline.composemvi.domain.error.AppError

/**
 * A generic class that holds a value with its loading status.
 *
 * @param D The return type of the [Result]
 * @param E The return type of the [Result] in case of business rule error
 */
sealed class Result<out D, out E> {
    data class Success<out D>(val data: D) : Result<D, Nothing>()
    data class Error(val error: AppError) : Result<Nothing, Nothing>()
    data class BusinessRuleError<out E>(val error: E) : Result<Nothing, E>()
    object Loading : Result<Nothing, Nothing>()

    fun isSuccessful() = this is Success
    fun hasFailed() = this is Error || this is BusinessRuleError<*>
    fun isLoading() = this is Loading

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            is BusinessRuleError<*> -> "BusinessRuleError[error=$error]"
            Loading -> "Loading"
        }
    }
}

/**
 * [Success.data][Result.Success.data] if [Result] is of type [Success][Result.Success]
 */
fun <D> Result<D, *>.successOr(fallback: D): D {
    return (this as? Result.Success<D>)?.data ?: fallback
}

inline fun <D> Result<D, *>.onSuccess(block: (D) -> Unit): Result<D, *> {
    if (this is Result.Success<D>) {
        block(data)
    }

    return this
}

inline fun <D, E> Result<D, E>.onBusinessRuleError(block: (E) -> Unit): Result<D, E> {
    if (this is Result.BusinessRuleError<E>) {
        block(error)
    }

    return this
}

inline fun <D> Result<D, *>.onError(block: (AppError) -> Unit): Result<D, *> {
    if (this is Result.Error) {
        block(error)
    }

    return this
}

inline fun <D> Result<D, *>.whenFinished(block: () -> Unit): Result<D, *> {
    block()
    return this
}

val <D> Result<D, *>.data: D?
    get() = (this as? Result.Success)?.data

/**
 * Updates value of [liveData] if [Result] is of query [Success]
 */
inline fun <reified D> Result<D, *>.updateOnSuccess(liveData: MutableLiveData<D>) {
    if (this is Result.Success) {
        liveData.value = data
    }
}
