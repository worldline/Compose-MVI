package com.worldline.composemvi.data.network

import com.worldline.composemvi.domain.error.NetworkException
import com.worldline.composemvi.domain.model.StatusCode
import java.net.UnknownHostException
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class NetworkErrorInterceptor @Inject constructor() : Interceptor {
    @Throws(NetworkException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response

        try {
            response = chain.proceed(request)

            if (!response.isSuccessful) {
                throw NetworkException(StatusCode.from(response.code))
            }
        } catch (e: UnknownHostException) {
            Timber.e(e)
            throw NetworkException(StatusCode.NoNetwork)
        }

        return response
    }
}