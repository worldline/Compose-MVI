package com.worldline.composemvi.domain.model.usecase

sealed class DataResult<out D, out E> {
    data class Success<out D>(val data: D) : DataResult<D, Nothing>()
    data class Error<out E>(val error: E) : DataResult<Nothing, E>()

    fun isSuccessful() = this is Success
    fun hasFailed() = this is Error

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$error]"
        }
    }
}
