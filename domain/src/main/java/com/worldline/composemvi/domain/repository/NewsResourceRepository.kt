package com.worldline.composemvi.domain.repository

import com.worldline.composemvi.domain.model.NewsResource
import com.worldline.composemvi.domain.model.usecase.DataResult

interface NewsResourceRepository {
    suspend fun getNewsResources(): DataResult<List<NewsResource>, NewsResourceRepositoryError>
    suspend fun getNewsResource(id: String): NewsResource

    sealed class NewsResourceRepositoryError {
        data object NoNewsResources : NewsResourceRepositoryError()
    }
}
