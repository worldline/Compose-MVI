package com.worldline.composemvi.domain.model

import kotlin.random.Random
import kotlinx.datetime.Instant

/**
 * External data layer representation of a fully populated NiA news resource
 */
data class NewsResource(
    val id: String,
    val title: String,
    val content: String,
    val url: String,
    val headerImageUrl: String?,
    val publishDate: Instant,
    val type: String,
    val topics: List<Topic>,
) {
    companion object {
        fun fake() = NewsResource(
            id = Random.nextInt().toString(),
            title = "Title",
            content = "Content",
            url = "https://android.com",
            headerImageUrl = "https://android.com/image.jpg",
            publishDate = Instant.fromEpochSeconds(0),
            type = "type",
            topics = listOf(
                Topic.fake(),
                Topic.fake()
            )
        )
    }
}