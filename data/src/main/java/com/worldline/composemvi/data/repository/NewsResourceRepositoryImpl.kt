package com.worldline.composemvi.data.repository

import com.worldline.composemvi.data.model.toDomain
import com.worldline.composemvi.data.network.MviApi
import com.worldline.composemvi.domain.model.NewsResource
import com.worldline.composemvi.domain.repository.NewsResourceRepository
import javax.inject.Inject

class NewsResourceRepositoryImpl @Inject constructor(
    private val mviApi: MviApi
) : NewsResourceRepository {
    override suspend fun getNewsResources(): List<NewsResource> {
        return mviApi.getNewsResources().map { it.toDomain() }
    }

    override suspend fun getNewsResource(id: String): NewsResource {
        return mviApi.getNewsResource(id).toDomain()
    }
}