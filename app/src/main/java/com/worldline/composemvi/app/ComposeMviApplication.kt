package com.worldline.composemvi.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.util.DebugLogger
import com.worldline.composemvi.app.conf.timberConf
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ComposeMviApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        timberConf()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components { add(SvgDecoder.Factory()) }
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}
