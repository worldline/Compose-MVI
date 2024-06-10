package com.worldline.composemvi.presentation.ui.foryou

import androidx.lifecycle.viewModelScope
import com.worldline.composemvi.domain.business.foryou.GetNewsUseCase
import com.worldline.composemvi.domain.business.foryou.GetTopicsUseCase
import com.worldline.composemvi.domain.model.Result
import com.worldline.composemvi.presentation.ui.base.BaseViewModel
import com.worldline.composemvi.presentation.ui.foryou.ForYouScreenReducer.ForYouEffect
import com.worldline.composemvi.presentation.ui.foryou.ForYouScreenReducer.ForYouEvent
import com.worldline.composemvi.presentation.ui.foryou.ForYouScreenReducer.ForYouState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val getTopicsUseCase: GetTopicsUseCase,
    private val getNewsUseCase: GetNewsUseCase
) : BaseViewModel<ForYouState, ForYouEvent, ForYouEffect>(
    initialState = ForYouState.initial(),
    reducer = ForYouScreenReducer()
) {
    init {
        viewModelScope.launch {
            getTopicsUseCase(Unit).collect { result ->
                sendEvent(
                    event = ForYouEvent.UpdateTopicsLoading(
                        isLoading = result.isLoading()
                    )
                )

                when (result) {
                    is Result.BusinessRuleError -> Unit
                    is Result.Error -> Unit
                    Result.Loading -> Unit
                    is Result.Success -> sendEvent(
                        event = ForYouEvent.UpdateTopics(
                            topics = result.data
                        )
                    )
                }
            }
        }

        viewModelScope.launch {
            getNewsUseCase(Unit).collect { result ->
                sendEvent(
                    event = ForYouEvent.UpdateNewsLoading(
                        isLoading = result.isLoading()
                    )
                )

                when (result) {
                    is Result.BusinessRuleError -> Unit
                    is Result.Error -> Unit
                    Result.Loading -> Unit
                    is Result.Success -> sendEvent(
                        event = ForYouEvent.UpdateNews(
                            news = result.data
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