package com.worldline.composemvi.presentation.utils

import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.fade
import com.google.accompanist.placeholder.material3.placeholder
import kotlinx.coroutines.flow.Flow

/**
 * Remembers in the Composition a flow that only emits data when `lifecycle` is
 * at least in `minActiveState`. That's achieved using the `Flow.flowWithLifecycle` operator.
 *
 * Explanation: If flows with operators in composable functions are not remembered, operators
 * will _always_ be called and applied on every recomposition.
 */
@Composable
fun <T> rememberFlowWithLifecycle(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = remember(flow, lifecycle) {
    flow.flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState
    )
}

/**
 * Apply a placeholder highlighting to a [Composable] is the [visible] property is set to true.
 *
 * @param visible whether or not to apply a placeholder to the parent [Composable]
 */
fun Modifier.fadePlaceholder(visible: Boolean): Modifier = composed {
    placeholder(
        visible = visible,
        highlight = PlaceholderHighlight.fade(),
    )
}

@Composable
fun requireActivity(): ComponentActivity {
    var currentContext = LocalContext.current
    while (currentContext is ContextWrapper) {
        if (currentContext is ComponentActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }

    error("Composable not attached to an activity.")
}
