package com.worldline.composemvi.data.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.worldline.composemvi.data.mock.MockManager
import com.worldline.composemvi.data.network.MviApi
import com.worldline.composemvi.data.repository.NewsResourceRepositoryImpl
import com.worldline.composemvi.data.repository.TopicsRepositoryImpl
import com.worldline.composemvi.domain.repository.NewsResourceRepository
import com.worldline.composemvi.domain.repository.TopicsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

@Module(includes = [DataModule.Declarations::class])
@InstallIn(SingletonComponent::class)
class DataModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class Declarations {
        @Binds
        abstract fun provideTopicsRepository(repository: TopicsRepositoryImpl): TopicsRepository

        @Binds
        abstract fun provideNewsResourceRepository(repository: NewsResourceRepositoryImpl): NewsResourceRepository
    }

    /**
     * Provides an API instance with Hilt dependency injection which users can use to request
     * specific endpoints.
     *
     * @param context The [ApplicationContext] provided by Hilt
     * @param moshi The [Moshi] instance provided by the [NetworkModule]
     * @param client The [OkHttpClient] instance provided by the [NetworkClientModule]
     * @return A [MviApi] instance
     */
    @Provides
    fun provideMviApi(
        @ApplicationContext context: Context,
        moshi: Moshi,
        client: OkHttpClient
    ): MviApi {
        val mockUrl = MockManager().init(context)
        Timber.d("Mock URL: $mockUrl")

        return Retrofit.Builder()
            .baseUrl(mockUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MviApi::class.java)
    }
}