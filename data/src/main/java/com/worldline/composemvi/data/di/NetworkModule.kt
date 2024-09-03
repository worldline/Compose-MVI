package com.worldline.composemvi.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.worldline.composemvi.data.network.adapters.InstantAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Date

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    /**
     * Provides a [Moshi] instance with Hilt dependency injection.
     *
     * This is where you can add custom Moshi adapters such as [EnumJsonAdapter].
     */
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .add(Date::class.java, Rfc3339DateJsonAdapter())
            .add(InstantAdapter)
            .build()
    }
}