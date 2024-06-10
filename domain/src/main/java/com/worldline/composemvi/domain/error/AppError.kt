package com.worldline.composemvi.domain.error

import com.worldline.composemvi.domain.model.StatusCode
import javax.net.ssl.SSLPeerUnverifiedException

/**
 * App error class that serves as a container for different error types.
 */
sealed class AppError {

    abstract fun toPrintableCode(): String
    abstract val originalException: Throwable

    /**
     * A simple error represented by a [String].
     *
     * @property message The message of the error
     * @property messageRes The string ID of the error
     */
    data class TextualError(
        val message: String? = null,
        val messageRes: Int? = null,
        override val originalException: Throwable
    ) : AppError() {

        @Suppress("StringTemplate")
        override fun toPrintableCode() = "$ERROR_CODE_PREFIX-$messageRes-$message"

        companion object {
            const val ERROR_CODE_PREFIX = 0
        }
    }

    /**
     * A common error represented by a [CommonErrorCode].
     *
     * @property code The [CommonErrorCode] for the error
     */
    data class CommonError(
        val code: CommonErrorCode,
        override val originalException: Throwable
    ) : AppError() {

        @Suppress("StringTemplate")
        override fun toPrintableCode() =
            "$ERROR_CODE_PREFIX-${code.code}".plus(originalException.message)

        companion object {
            const val ERROR_CODE_PREFIX = 1
        }
    }

    /**
     * A network error represented by a [StatusCode].
     *
     * @property statusCode The [StatusCode] for the error
     */
    data class NetworkError(
        val statusCode: StatusCode,
        override val originalException: Throwable
    ) : AppError() {

        @Suppress("StringTemplate")
        override fun toPrintableCode() = "$ERROR_CODE_PREFIX-${statusCode.code}"

        companion object {
            const val ERROR_CODE_PREFIX = 2
        }
    }
}

/**
 * Map an [Exception] to an [AppError].
 *
 * @return The associated [AppError.NetworkError] or [AppError.CommonError]
 */
fun Throwable.mapToAppError(): AppError {
    return when (this) {
        is NetworkException -> AppError.NetworkError(code, this)
        else -> mapToCommonError()
    }
}

/**
 * Map an [Exception] to a [AppError.CommonError].
 *
 * @return the associated [AppError.CommonError]
 */
fun Throwable.mapToCommonError(): AppError.CommonError {
    val code = when (this) {
        is NullPointerException -> CommonErrorCode.NullPointerException
        is IllegalStateException -> CommonErrorCode.IllegalStateException
        is IllegalArgumentException -> CommonErrorCode.IllegalArgumentException
        is ArrayIndexOutOfBoundsException -> CommonErrorCode.ArrayIndexOutOfBoundsException
        is SSLPeerUnverifiedException -> CommonErrorCode.SecurityCheck
        else -> CommonErrorCode.Unknown
    }

    return AppError.CommonError(
        code = code,
        originalException = this
    )
}
