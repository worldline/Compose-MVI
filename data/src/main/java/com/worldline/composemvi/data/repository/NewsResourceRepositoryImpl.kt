package com.worldline.composemvi.data.repository

import com.worldline.composemvi.data.model.toDomain
import com.worldline.composemvi.data.network.MviApi
import com.worldline.composemvi.data.network.safeApiCall
import com.worldline.composemvi.domain.model.NewsResource
import com.worldline.composemvi.domain.model.StatusCode
import com.worldline.composemvi.domain.model.usecase.DataResult
import com.worldline.composemvi.domain.repository.NewsResourceRepository
import javax.inject.Inject

class NewsResourceRepositoryImpl @Inject constructor(
    private val mviApi: MviApi
) : NewsResourceRepository {
    override suspend fun getNewsResources(): DataResult<List<NewsResource>, NewsResourceRepository.NewsResourceRepositoryError> {
        return safeApiCall(
            apiCall = { mviApi.getNewsResources().map { it.toDomain() } },
            onError = { statusCode ->
                when (statusCode) {
                    StatusCode.NoContent -> NewsResourceRepository.NewsResourceRepositoryError.NoNewsResources
                    else -> null
                }
            }
        )
    }

    override suspend fun getNewsResource(id: String): NewsResource {
        return mviApi.getNewsResource(id).toDomain()
    }
}