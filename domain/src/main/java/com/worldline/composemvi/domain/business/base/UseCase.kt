package com.worldline.composemvi.domain.business.base

import android.util.Log
import com.worldline.composemvi.domain.error.mapToAppError
import com.worldline.composemvi.domain.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Abstract class representing a basic use case. A Use Case is a class that represents a single task
 * that the user can perform. It is a simple class that receives a request and returns a response.
 *
 * This specific class returns a [Result] object directly that wraps the result of the request. It
 * is intended to be used in cases where the request either doesn't have a result or the result is
 * obtainable synchronously. For example, a request to delete a user from the database should be
 * called via a [UseCase]. (Note that request here can be a request to a database, network, or any
 * other)
 *
 * The [BusinessRuleError] parameter is intended to represent business logic errors specific to this use case
 * and should be implemented in the following manner:
 *
 * ```kotlin
 * class MyUseCase(
 *     private val repository: MyRepository,
 *     coroutineDispatcher: CoroutineDispatcher
 * ) : UseCase<MyParameters, MySuccess, MyUseCaseError>(coroutineDispatcher) {
 *     sealed class MyUseCaseError {
 *         object MyBusinessError : MyUseCaseError()
 *     }
 *
 *     override suspend fun execute(parameters: MyParameters): WLResult<MySuccess, MyUseCaseError> {
 *         val result = repository.doSomething(parameters)
 *         return if (result == null) {
 *             WLResult.BusinessRuleError(MyUseCaseError.MyBusinessError)
 *         } else {
 *             WLResult.Success(result)
 *         }
 *     }
 * }
 * ```
 *
 * Once implemented, the use case can be called from a ViewModel as follows:
 *
 * ```kotlin
 * fun callMyUseCase(parameters: MyParameters) {
 *     viewModelScope.coroutineScope.launch {
 *         myUseCase(parameters).onSuccess { result ->
 *             // Handle the result
 *         }.onBusinessRuleError { error ->
 *             // Handle the business rule error
 *         }.onError { error ->
 *             // Handle the error
 *         }
 *     }
 * }
 * ```
 *
 * @param Parameters The input parameters type to the use case.
 * @param Success The success output type of the use case.
 * @param BusinessRuleError The business rule error output type of the use case.
 * @property dispatcher The [CoroutineDispatcher] that will be used to execute the use case.
 */
abstract class UseCase<in Parameters, Success, BusinessRuleError>(private val dispatcher: CoroutineDispatcher) {

    /** Executes the use case asynchronously and returns a [Result].
     *
     * @param parameters The input parameters to run the use case with
     * @return A [Result].
     */
    @Suppress("TooGenericExceptionCaught")
    suspend operator fun invoke(parameters: Parameters): Result<Success, BusinessRuleError> {
        return try {
            // Moving all use case's executions to the injected dispatcher
            // In production code, this is usually the Default dispatcher (background thread)
            // In tests, this becomes a TestCoroutineDispatcher
            withContext(dispatcher) {
                execute(parameters)
            }
        } catch (e: RuntimeException) {
            Log.e("UseCase", "An error occurred while executing the use case", e)
            Result.Error(e.mapToAppError())
        }
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: Parameters): Result<Success, BusinessRuleError>
}
