package com.worldline.composemvi.data.di

import com.worldline.composemvi.data.network.NetworkErrorInterceptor
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
class NetworkClientModule {

    /**
     * Provides an [OkHttpClient] with Hilt dependency injection.
     */
    fun provideHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(TIMEOUT_IN_S, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_S, TimeUnit.SECONDS)
            .addInterceptor(NetworkErrorInterceptor())

        return builder.build()
    }

    companion object {
        private const val TIMEOUT_IN_S: Long = 15
    }
}
