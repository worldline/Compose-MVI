package com.worldline.composemvi.domain.repository

import com.worldline.composemvi.domain.model.Topic

interface TopicsRepository {
    suspend fun getTopics(): List<Topic>
    suspend fun getTopic(id: String): Topic
}
