package com.worldline.composemvi.domain.business.foryou

import com.worldline.composemvi.domain.business.base.FlowUseCase
import com.worldline.composemvi.domain.di.IoDispatcher
import com.worldline.composemvi.domain.model.Result
import com.worldline.composemvi.domain.model.UserData
import com.worldline.composemvi.domain.model.UserNewsResource
import com.worldline.composemvi.domain.model.mapToUserNewsResources
import com.worldline.composemvi.domain.repository.NewsResourceRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNewsUseCase @Inject constructor(
    private val repository: NewsResourceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<UserNewsResource>, GetNewsUseCase.GetNewsErrors>(dispatcher) {

    sealed class GetNewsErrors {
        data object NoNewsFound : GetNewsErrors()
    }

    override fun execute(parameters: Unit): Flow<Result<List<UserNewsResource>, GetNewsErrors>> {
        return flow {
            emit(Result.Loading)

            val news = repository.getNewsResources()

            if (news.isEmpty()) {
                emit(Result.BusinessRuleError(GetNewsErrors.NoNewsFound))
                return@flow
            } else {
                val userNews = news.mapToUserNewsResources(UserData.fake())
                emit(Result.Success(userNews))
            }
        }
    }
}