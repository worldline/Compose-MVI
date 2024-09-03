package com.worldline.composemvi.data.repository

import com.worldline.composemvi.data.model.toDomain
import com.worldline.composemvi.data.network.MviApi
import com.worldline.composemvi.domain.model.Topic
import com.worldline.composemvi.domain.repository.TopicsRepository
import javax.inject.Inject

class TopicsRepositoryImpl @Inject constructor(
    private val mviApi: MviApi
) : TopicsRepository {
    override suspend fun getTopics(): List<Topic> {
        return mviApi.getTopics().map { it.toDomain() }
    }

    override suspend fun getTopic(id: String): Topic {
        return mviApi.getTopic(id).toDomain()
    }
}