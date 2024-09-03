package com.worldline.composemvi.data.di

import com.worldline.composemvi.data.network.NetworkErrorInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

@Module
@InstallIn(SingletonComponent::class)
class NetworkClientModule {

    /**
     * Provides a [HttpLoggingInterceptor] with Hilt dependency injection.
     */
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message -> Timber.tag("network").v(message) }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /**
     * Provides an [OkHttpClient] with Hilt dependency injection.
     *
     * @param loggingInterceptor A [HttpLoggingInterceptor] used to log network requests
     */
    @Provides
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .readTimeout(TIMEOUT_IN_S, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_S, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(NetworkErrorInterceptor())

        return builder.build()
    }

    companion object {
        private const val TIMEOUT_IN_S: Long = 15
    }
}
