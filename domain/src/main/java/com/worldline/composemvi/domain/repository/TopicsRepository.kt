package com.worldline.composemvi.domain.repository

import com.worldline.composemvi.domain.model.Topic
import com.worldline.composemvi.domain.model.usecase.DataResult

interface TopicsRepository {
    suspend fun getTopics(): DataResult<List<Topic>, TopicsRepositoryError>
    suspend fun getTopic(id: String): Topic

    sealed class TopicsRepositoryError {
        data object NoTopics : TopicsRepositoryError()
    }
}
