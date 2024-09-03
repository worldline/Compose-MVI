package com.worldline.composemvi.data.mock.internal

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import timber.log.Timber

class MockWebServerDispatcher(
    private val assets: AssetProvider
) : Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        val path = request.path.orEmpty()

        return when {
            path == "/topics" -> assets.createResponseFromAssets("topics.json")
            path == "/newsresources" -> assets.createResponseFromAssets("news.json")
            TOPIC_REGEX.matches(path) -> assets.createResponseFromAssets("topic_by_id.json")
            NEWS_REGEX.matches(path) -> assets.createResponseFromAssets("news_by_id.json")

            else -> {
                Timber.w(
                    "Mocked URL not handled for path (%s), returning empty response",
                    request.path
                )
                assets.createEmptyResponse()
            }
        }
    }

    companion object {
        private val TOPIC_REGEX = Regex("/topics/.*")
        private val NEWS_REGEX = Regex("/newsresources/.*")
    }
}