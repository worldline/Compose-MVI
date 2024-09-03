package com.worldline.composemvi.data.mock.internal

import android.content.res.AssetManager
import com.worldline.composemvi.domain.model.StatusCode
import java.util.Scanner
import java.util.concurrent.TimeUnit
import okhttp3.mockwebserver.MockResponse

class AssetProvider(
    private val assetManager: AssetManager
) {

    /**
     * Create a [MockResponse] object from a file.
     *
     * @param fileName The name of the file from which to create a [MockResponse]. This must contain
     * the file extension (.json, .txt, etc.)
     * @param statusCode The [StatusCode] to apply to said response. The default value is
     * [StatusCode.OK]
     * @param delayInMs The delay with which to return the body of the [MockResponse]
     */
    fun createResponseFromAssets(
        fileName: String,
        statusCode: StatusCode = StatusCode.OK,
        delayInMs: Long = DEFAULT_DELAY_IN_MS
    ): MockResponse {
        val inputStream = assetManager.open(fileName)

        val s = Scanner(inputStream, Charsets.UTF_8.name()).useDelimiter("\\A")
        val result = if (s.hasNext()) s.next() else ""

        return MockResponse()
            .setBody(result)
            .setResponseCode(statusCode.code)
            .setBodyDelay(delayInMs, TimeUnit.MILLISECONDS)
    }

    /**
     * Create a [MockResponse] object from an input.
     *
     * @param content The content from which to create a [MockResponse].
     * @param statusCode The [StatusCode] to apply to said response. The default value is
     * [StatusCode.OK]
     * @param delayInMs The delay with which to return the body of the [MockResponse]
     */
    fun createResponse(
        content: String,
        statusCode: StatusCode = StatusCode.OK,
        delayInMs: Long = DEFAULT_DELAY_IN_MS
    ): MockResponse =
        MockResponse()
            .setBody(content)
            .setResponseCode(statusCode.code)
            .setBodyDelay(delayInMs, TimeUnit.MILLISECONDS)

    /**
     * Create an empty [MockResponse].
     *
     * @param statusCode The [StatusCode] to apply to said response. The default value is
     * [StatusCode.OK]
     * @param delayInMs The delay with which to return the body of the [MockResponse]
     */
    fun createEmptyResponse(
        statusCode: StatusCode = StatusCode.OK,
        delayInMs: Long = DEFAULT_DELAY_IN_MS
    ): MockResponse =
        MockResponse()
            .setBody("")
            .setResponseCode(statusCode.code)
            .setHeadersDelay(delayInMs, TimeUnit.MILLISECONDS)

    /**
     * Private function to create a [MockResponse] based on whether the [fileName] is null
     *
     * @param fileName The name of the file from which to create the response or null
     * @param successCode The success [StatusCode] to apply to the [MockResponse]
     */
    private fun createResponseFromAssetsOrEmpty(
        fileName: String?,
        successCode: StatusCode
    ): MockResponse {
        return if (fileName != null) {
            createResponseFromAssets(fileName, successCode)
        } else {
            createEmptyResponse(successCode)
        }
    }

    companion object {
        private const val DEFAULT_DELAY_IN_MS = 1_000L
    }
}
