package com.worldline.composemvi.presentation.ui.foryou

import com.worldline.composemvi.domain.business.foryou.GetForYouDataUseCase
import com.worldline.composemvi.domain.model.usecase.Result
import com.worldline.composemvi.presentation.ui.base.BaseViewModel
import com.worldline.composemvi.presentation.ui.foryou.ForYouScreenReducer.ForYouEffect
import com.worldline.composemvi.presentation.ui.foryou.ForYouScreenReducer.ForYouEvent
import com.worldline.composemvi.presentation.ui.foryou.ForYouScreenReducer.ForYouState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import timber.log.Timber

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val getForYouDataUseCase: GetForYouDataUseCase
) : BaseViewModel<ForYouState, ForYouEvent, ForYouEffect>(
    initialState = ForYouState.initial(),
    reducer = ForYouScreenReducer()
) {

    override suspend fun initialDataLoad() {
        getForYouDataUseCase(Unit).collect { result ->
            sendEvent(
                event = ForYouEvent.UpdateLoading(
                    isLoading = result.isLoading()
                )
            )

            when (result) {
                is Result.BusinessRuleError -> when (result.error) {
                    GetForYouDataUseCase.GetForYouDataErrors.NoNewsFound -> {
                        Timber.e("BusinessRuleError: No news found")
                    }

                    GetForYouDataUseCase.GetForYouDataErrors.NoTopicsFound -> {
                        Timber.e("BusinessRuleError: No topics found")
                    }
                }

                is Result.Error -> {
                    Timber.e("Error: ${result.error}")
                }

                is Result.Loading -> Unit
                is Result.Success -> when (val data = result.data) {
                    is GetForYouDataUseCase.GetForYouDataSuccess.NewsData -> sendEvent(
                        event = ForYouEvent.UpdateNews(
                            news = data.news
                        )
                    )

                    is GetForYouDataUseCase.GetForYouDataSuccess.TopicsData -> sendEvent(
                        event = ForYouEvent.UpdateTopics(
                            topics = data.topics
                        )
                    )
                }
            }
        }
    }

    fun onTopicClick(topicId: String) {
        sendEffect(
            effect = ForYouEffect.NavigateToTopic(
                topicId = topicId
            )
        )
    }
}