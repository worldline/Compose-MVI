package com.worldline.composemvi.presentation.ui.foryou

import androidx.compose.runtime.Immutable
import com.worldline.composemvi.domain.model.FollowableTopic
import com.worldline.composemvi.domain.model.UserNewsResource
import com.worldline.composemvi.presentation.ui.base.Reducer

class ForYouScreenReducer :
    Reducer<ForYouScreenReducer.ForYouState, ForYouScreenReducer.ForYouEvent, ForYouScreenReducer.ForYouEffect> {
    @Immutable
    sealed class ForYouEvent : Reducer.ViewEvent {
        data class UpdateLoading(val isLoading: Boolean) : ForYouEvent()
        data class UpdateTopics(val topics: List<FollowableTopic>) : ForYouEvent()
        data class UpdateNews(val news: List<UserNewsResource>) : ForYouEvent()
    }

    @Immutable
    sealed class ForYouEffect : Reducer.ViewEffect

    @Immutable
    data class ForYouState(
        val isLoading: Boolean,
        val topics: List<FollowableTopic>,
        val news: List<UserNewsResource>
    ) : Reducer.ViewState {
        companion object {
            fun initial(): ForYouState {
                return ForYouState(
                    isLoading = true,
                    topics = listOf(
                        FollowableTopic.fake(),
                        FollowableTopic.fake(),
                        FollowableTopic.fake(),
                        FollowableTopic.fake(),
                        FollowableTopic.fake(),
                        FollowableTopic.fake(),
                        FollowableTopic.fake(),
                        FollowableTopic.fake()
                    ),
                    news = listOf(
                        UserNewsResource.fake(),
                        UserNewsResource.fake(),
                        UserNewsResource.fake(),
                        UserNewsResource.fake()
                    )
                )
            }
        }
    }

    override fun reduce(
        previousState: ForYouState,
        event: ForYouEvent
    ): Pair<ForYouState, ForYouEffect?> {
        return when (event) {
            is ForYouEvent.UpdateLoading -> {
                previousState.copy(
                    isLoading = event.isLoading
                ) to null
            }

            is ForYouEvent.UpdateTopics -> {
                previousState.copy(
                    topics = event.topics
                ) to null
            }

            is ForYouEvent.UpdateNews -> {
                previousState.copy(
                    news = event.news
                ) to null
            }
        }
    }
}