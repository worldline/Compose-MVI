package com.worldline.composemvi.domain.business.foryou

import com.worldline.composemvi.domain.business.base.FlowUseCase
import com.worldline.composemvi.domain.di.IoDispatcher
import com.worldline.composemvi.domain.model.Result
import com.worldline.composemvi.domain.model.UserNewsResource
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNewsUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<UserNewsResource>, Nothing>(dispatcher) {
    override fun execute(parameters: Unit): Flow<Result<List<UserNewsResource>, Nothing>> {
        return flow {
            emit(Result.Loading)
            delay(1000)
            emit(Result.Success(listOf(
                UserNewsResource.fake(),
                UserNewsResource.fake(),
                UserNewsResource.fake(),
                UserNewsResource.fake()
            ).distinctBy { it.id }
            ))
        }
    }
}