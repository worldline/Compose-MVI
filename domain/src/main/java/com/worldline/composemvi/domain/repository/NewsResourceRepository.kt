package com.worldline.composemvi.domain.repository

import com.worldline.composemvi.domain.model.NewsResource

interface NewsResourceRepository {
    suspend fun getNewsResources(): List<NewsResource>
    suspend fun getNewsResource(id: String): NewsResource
}
