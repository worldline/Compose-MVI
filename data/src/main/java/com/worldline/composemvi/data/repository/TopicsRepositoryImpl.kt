package com.worldline.composemvi.data.repository

import com.worldline.composemvi.data.model.toDomain
import com.worldline.composemvi.data.network.MviApi
import com.worldline.composemvi.data.network.safeApiCall
import com.worldline.composemvi.domain.model.StatusCode
import com.worldline.composemvi.domain.model.Topic
import com.worldline.composemvi.domain.model.usecase.DataResult
import com.worldline.composemvi.domain.repository.TopicsRepository
import com.worldline.composemvi.domain.repository.TopicsRepository.TopicsRepositoryError
import javax.inject.Inject

class TopicsRepositoryImpl @Inject constructor(
    private val mviApi: MviApi
) : TopicsRepository {

    override suspend fun getTopics(): DataResult<List<Topic>, TopicsRepositoryError> {
        return safeApiCall(
            apiCall = { mviApi.getTopics().map { it.toDomain() } },
            onError = { statusCode ->
                when (statusCode) {
                    StatusCode.NoContent -> TopicsRepositoryError.NoTopics
                    else -> null
                }
            }
        )
    }

    override suspend fun getTopic(id: String): Topic {
        return mviApi.getTopic(id).toDomain()
    }
}