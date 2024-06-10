package com.worldline.composemvi.presentation.ui.foryou

import androidx.compose.runtime.Immutable
import com.worldline.composemvi.domain.model.FollowableTopic
import com.worldline.composemvi.domain.model.UserNewsResource
import com.worldline.composemvi.presentation.ui.base.Reducer

class ForYouScreenReducer :
    Reducer<ForYouScreenReducer.ForYouState, ForYouScreenReducer.ForYouEvent, ForYouScreenReducer.ForYouEffect> {
    @Immutable
    sealed class ForYouEvent : Reducer.ViewEvent {
        data class UpdateTopicsLoading(val isLoading: Boolean) : ForYouEvent()
        data class UpdateTopics(val topics: List<FollowableTopic>) : ForYouEvent()
        data class UpdateNewsLoading(val isLoading: Boolean) : ForYouEvent()
        data class UpdateNews(val news: List<UserNewsResource>) : ForYouEvent()
        data class UpdateTopicIsFollowed(val topicId: String, val isFollowed: Boolean) :
            ForYouEvent()

        data class UpdateNewsIsSaved(val newsId: String, val isSaved: Boolean) : ForYouEvent()
        data class UpdateNewsIsViewed(val newsId: String, val isViewed: Boolean) : ForYouEvent()
        data class UpdateTopicsVisible(val isVisible: Boolean) : ForYouEvent()
    }

    @Immutable
    sealed class ForYouEffect : Reducer.ViewEffect {
        data class NavigateToTopic(val topicId: String) : ForYouEffect()
        data class NavigateToNews(val newsUrl: String) : ForYouEffect()
    }

    @Immutable
    data class ForYouState(
        val topicsLoading: Boolean,
        val newsLoading: Boolean,
        val topicsVisible: Boolean,
        val topics: List<FollowableTopic>,
        val news: List<UserNewsResource>
    ) : Reducer.ViewState {
        companion object {
            fun initial(): ForYouState {
                val topics = listOf(
                    FollowableTopic.fake(),
                    FollowableTopic.fake(),
                    FollowableTopic.fake(),
                    FollowableTopic.fake(),
                    FollowableTopic.fake(),
                    FollowableTopic.fake(),
                    FollowableTopic.fake(),
                    FollowableTopic.fake()
                ).distinctBy { it.topic.id }

                val news = listOf(
                    UserNewsResource.fake(),
                    UserNewsResource.fake(),
                    UserNewsResource.fake(),
                    UserNewsResource.fake()
                ).distinctBy { it.id }

                return ForYouState(
                    topicsLoading = true,
                    newsLoading = true,
                    topicsVisible = topics.all { !it.isFollowed },
                    topics = topics,
                    news = news
                )
            }
        }
    }

    override fun reduce(
        previousState: ForYouState,
        event: ForYouEvent
    ): Pair<ForYouState, ForYouEffect?> {
        return when (event) {
            is ForYouEvent.UpdateTopicsLoading -> {
                previousState.copy(
                    topicsLoading = event.isLoading
                ) to null
            }

            is ForYouEvent.UpdateTopics -> {
                previousState.copy(
                    topics = event.topics
                ) to null
            }

            is ForYouEvent.UpdateNewsLoading -> {
                previousState.copy(
                    newsLoading = event.isLoading
                ) to null
            }

            is ForYouEvent.UpdateNews -> {
                previousState.copy(
                    news = event.news
                ) to null
            }

            is ForYouEvent.UpdateTopicIsFollowed -> {
                val updatedTopics = previousState.topics.map { topic ->
                    if (topic.topic.id == event.topicId) {
                        topic.copy(isFollowed = event.isFollowed)
                    } else {
                        topic
                    }
                }
                previousState.copy(
                    topics = updatedTopics
                ) to null
            }

            is ForYouEvent.UpdateNewsIsSaved -> {
                val updatedNews = previousState.news.map { news ->
                    if (news.id == event.newsId) {
                        news.copy(isSaved = event.isSaved)
                    } else {
                        news
                    }
                }
                previousState.copy(
                    news = updatedNews
                ) to null
            }

            is ForYouEvent.UpdateNewsIsViewed -> {
                val updatedNews = previousState.news.map { news ->
                    if (news.id == event.newsId) {
                        news.copy(hasBeenViewed = event.isViewed)
                    } else {
                        news
                    }
                }
                previousState.copy(
                    news = updatedNews
                ) to ForYouEffect.NavigateToNews(updatedNews.first { it.id == event.newsId }.url)
            }

            is ForYouEvent.UpdateTopicsVisible -> {
                previousState.copy(
                    topicsVisible = event.isVisible
                ) to null
            }
        }
    }
}