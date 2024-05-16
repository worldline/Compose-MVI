package com.worldline.composemvi.presentation.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.worldline.composemvi.domain.analytics.AnalyticsHelper
import com.worldline.composemvi.domain.analytics.NoOpAnalyticsHelper

/**
 * Global key used to obtain access to the AnalyticsHelper through a CompositionLocal.
 */
val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    // Provide a default AnalyticsHelper which does nothing. This is so that tests and previews
    // do not have to provide one. For real app builds provide a different implementation.
    NoOpAnalyticsHelper()
}