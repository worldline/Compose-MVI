package com.worldline.composemvi.data.model

import com.squareup.moshi.JsonClass
import com.worldline.composemvi.domain.model.NewsResource
import kotlinx.datetime.Instant

@JsonClass(generateAdapter = true)
data class NetworkNewsResource(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val headerImageUrl: String,
    val publishDate: Instant,
    val type: String,
    val topics: List<String> = listOf(),
)

fun NetworkNewsResource.toDomain() = NewsResource(
    id = id,
    title = title,
    content = content,
    url = url,
    headerImageUrl = headerImageUrl,
    publishDate = publishDate,
    type = type,
    topics = emptyList()
)
