package com.worldline.composemvi.data.network

import com.worldline.composemvi.data.model.NetworkNewsResource
import com.worldline.composemvi.data.model.NetworkTopic
import retrofit2.http.GET
import retrofit2.http.Path

interface MviApi {
    @GET("topics")
    suspend fun getTopics(): List<NetworkTopic>

    @GET("topics/{id}")
    suspend fun getTopic(@Path("id") id: String): NetworkTopic

    @GET("newsresources")
    suspend fun getNewsResources(): List<NetworkNewsResource>

    @GET("newsresources/{id}")
    suspend fun getNewsResource(@Path("id") id: String): NetworkNewsResource
}
