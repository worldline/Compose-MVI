package com.worldline.composemvi.domain.business.foryou

import com.worldline.composemvi.domain.business.base.FlowUseCase
import com.worldline.composemvi.domain.di.IoDispatcher
import com.worldline.composemvi.domain.model.FollowableTopic
import com.worldline.composemvi.domain.model.Result
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTopicsUseCase @Inject constructor(
//    private val topicsRepository: TopicsRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<FollowableTopic>, Nothing>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<FollowableTopic>, Nothing>> {
        return flow {
            emit(Result.Loading)
            delay(500)
            emit(Result.Success(listOf(
                FollowableTopic.fake(),
                FollowableTopic.fake(),
                FollowableTopic.fake(),
                FollowableTopic.fake(),
                FollowableTopic.fake(),
                FollowableTopic.fake(),
                FollowableTopic.fake(),
                FollowableTopic.fake()
            ).distinctBy { it.topic.id }
            ))
        }
    }
}