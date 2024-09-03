package com.worldline.composemvi.domain.business.foryou

import com.worldline.composemvi.domain.business.base.FlowUseCase
import com.worldline.composemvi.domain.di.IoDispatcher
import com.worldline.composemvi.domain.model.FollowableTopic
import com.worldline.composemvi.domain.model.Result
import com.worldline.composemvi.domain.model.UserData
import com.worldline.composemvi.domain.model.mapToFollowableTopic
import com.worldline.composemvi.domain.repository.TopicsRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTopicsUseCase @Inject constructor(
    private val repository: TopicsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<FollowableTopic>, GetTopicsUseCase.GetTopicsErrors>(dispatcher) {

    sealed class GetTopicsErrors {
        data object NoTopicsFound : GetTopicsErrors()
    }

    override fun execute(parameters: Unit): Flow<Result<List<FollowableTopic>, GetTopicsErrors>> {
        return flow {
            emit(Result.Loading)

            val topics = repository.getTopics()

            if (topics.isEmpty()) {
                emit(Result.BusinessRuleError(GetTopicsErrors.NoTopicsFound))
                return@flow
            } else {
                val followableTopics = topics.mapToFollowableTopic(UserData.fake())
                emit(Result.Success(followableTopics))
            }
        }
    }
}