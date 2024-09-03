package com.worldline.composemvi.data.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import kotlinx.datetime.Instant

object InstantAdapter {
    @ToJson
    fun toJson(instant: Instant): String {
        return instant.toString()
    }

    @FromJson
    fun fromJson(instant: String): Instant {
        return Instant.parse(instant)
    }
}