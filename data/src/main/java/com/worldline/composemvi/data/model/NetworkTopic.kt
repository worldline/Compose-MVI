package com.worldline.composemvi.data.model

import com.squareup.moshi.JsonClass
import com.worldline.composemvi.domain.model.Topic

@JsonClass(generateAdapter = true)
data class NetworkTopic(
    val id: String,
    val name: String = "",
    val shortDescription: String = "",
    val longDescription: String = "",
    val url: String = "",
    val imageUrl: String = "",
    val followed: Boolean = false
)

fun NetworkTopic.toDomain() = Topic(
    id = id,
    name = name,
    shortDescription = shortDescription,
    longDescription = longDescription,
    url = url,
    imageUrl = imageUrl
)
