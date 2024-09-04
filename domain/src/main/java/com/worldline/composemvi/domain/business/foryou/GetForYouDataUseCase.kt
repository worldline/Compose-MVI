package com.worldline.composemvi.domain.business.foryou

import com.worldline.composemvi.domain.business.base.FlowUseCase
import com.worldline.composemvi.domain.business.foryou.GetForYouDataUseCase.GetForYouDataErrors
import com.worldline.composemvi.domain.business.foryou.GetForYouDataUseCase.GetForYouDataSuccess
import com.worldline.composemvi.domain.di.IoDispatcher
import com.worldline.composemvi.domain.model.FollowableTopic
import com.worldline.composemvi.domain.model.UserData
import com.worldline.composemvi.domain.model.UserNewsResource
import com.worldline.composemvi.domain.model.mapToFollowableTopic
import com.worldline.composemvi.domain.model.mapToUserNewsResources
import com.worldline.composemvi.domain.model.usecase.DataResult
import com.worldline.composemvi.domain.model.usecase.Result
import com.worldline.composemvi.domain.repository.NewsResourceRepository
import com.worldline.composemvi.domain.repository.TopicsRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetForYouDataUseCase @Inject constructor(
    private val topicsRepository: TopicsRepository,
    private val newsResourceRepository: NewsResourceRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, GetForYouDataSuccess, GetForYouDataErrors>(dispatcher) {

    sealed class GetForYouDataErrors {
        data object NoTopicsFound : GetForYouDataErrors()
        data object NoNewsFound : GetForYouDataErrors()
    }

    sealed class GetForYouDataSuccess {
        data class TopicsData(val topics: List<FollowableTopic>) : GetForYouDataSuccess()
        data class NewsData(val news: List<UserNewsResource>) : GetForYouDataSuccess()
    }

    override fun execute(parameters: Unit): Flow<Result<GetForYouDataSuccess, GetForYouDataErrors>> {
        return flow {
            emit(Result.Loading)

            val topicsResult = topicsRepository.getTopics()
            val newsResult = newsResourceRepository.getNewsResources()

            when (topicsResult) {
                is DataResult.Error -> when (topicsResult.error) {
                    TopicsRepository.TopicsRepositoryError.NoTopics ->
                        emit(Result.BusinessRuleError(GetForYouDataErrors.NoTopicsFound))
                }

                is DataResult.Success -> emit(
                    Result.Success(
                        GetForYouDataSuccess.TopicsData(
                            topicsResult.data.mapToFollowableTopic(
                                UserData.fake()
                            )
                        )
                    )
                )
            }

            when (newsResult) {
                is DataResult.Error -> when (newsResult.error) {
                    NewsResourceRepository.NewsResourceRepositoryError.NoNewsResources ->
                        emit(Result.BusinessRuleError(GetForYouDataErrors.NoNewsFound))

                }

                is DataResult.Success -> emit(
                    Result.Success(
                        GetForYouDataSuccess.NewsData(
                            newsResult.data.mapToUserNewsResources(
                                UserData.fake()
                            )
                        )
                    )
                )
            }
        }
    }
}
