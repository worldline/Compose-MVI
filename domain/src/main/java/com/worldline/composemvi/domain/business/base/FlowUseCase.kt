package com.worldline.composemvi.domain.business.base

import android.util.Log
import com.worldline.composemvi.domain.error.mapToAppError
import com.worldline.composemvi.domain.model.usecase.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

/**
 * Abstract class representing a more complex use case. A Use Case is a class that represents a
 * single task that the user can perform. It is a simple class that receives a request and returns
 * a response.
 *
 * This specific class returns a [WLResult] object wrapped in a [Flow] that wraps the result of the
 * request. It is intended to be used in cases where the request has a result that is obtainable
 * asynchronously. For example, a request to get a user from the database should be called via a
 * [FlowUseCase]. (Note that request here can be a request to a database, network, or any other)
 *
 * The [BusinessRuleError] parameter is intended to represent business logic errors specific to this use case
 * and should be implemented in the following manner:
 *
 * ```kotlin
 * class MyUseCase(
 *     private val repository: MyRepository,
 *     coroutineDispatcher: CoroutineDispatcher
 * ) : FlowUseCase<MyParameters, MySuccess, MyUseCaseError>(coroutineDispatcher), KoinComponent {
 *     sealed class MyUseCaseError {
 *         object MyBusinessError : MyUseCaseError()
 *     }
 *
 *     override fun execute(parameters: MyParameters): Flow<WLResult<MySuccess, MyUseCaseError>> {
 *         return flow {
 *             emit(WLResult.Loading)
 *             val result = repository.doSomething(parameters)
 *             return if (result == null) {
 *                 emit(WLResult.BusinessRuleError(MyUseCaseError.MyBusinessError))
 *             } else {
 *                 emit(WLResult.Success(result))
 *             }
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
 *         myUseCase(parameters).collect { result ->
 *             when (result) {
 *                 // Handle the result
 *                 is WLResult.BusinessRuleError -> {
 *                     // Handle the business rule error
 *                     // Note that in this lambda, the result.error parameter is of type MyUseCaseError
 *                 }
 *             }
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
abstract class FlowUseCase<in Parameters, Success, BusinessRuleError>(private val dispatcher: CoroutineDispatcher) {

    operator fun invoke(parameters: Parameters): Flow<Result<Success, BusinessRuleError>> {
        return execute(parameters)
            .catch { e ->
                Log.e("FlowUseCase", "An error occurred while executing the use case", e)
                emit(Result.Error(e.mapToAppError()))
            }
            .flowOn(dispatcher)
    }

    abstract fun execute(parameters: Parameters): Flow<Result<Success, BusinessRuleError>>
}
