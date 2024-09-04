package com.worldline.composemvi.presentation.ui.foryou

import android.net.Uri
import android.util.Log
import androidx.activity.compose.ReportDrawnWhen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.worldline.composemvi.domain.model.FollowableTopic
import com.worldline.composemvi.domain.model.UserNewsResource
import com.worldline.composemvi.presentation.R
import com.worldline.composemvi.presentation.ui.components.DraggableScrollbar
import com.worldline.composemvi.presentation.ui.components.NewsResourceCardExpanded
import com.worldline.composemvi.presentation.ui.components.NiaButton
import com.worldline.composemvi.presentation.ui.components.NiaOverlayLoadingWheel
import com.worldline.composemvi.presentation.ui.components.NotificationPermissionEffect
import com.worldline.composemvi.presentation.ui.components.TopicSelection
import com.worldline.composemvi.presentation.ui.components.rememberDraggableScroller
import com.worldline.composemvi.presentation.ui.components.scrollbarState
import com.worldline.composemvi.presentation.ui.foryou.ForYouScreenReducer.ForYouEffect
import com.worldline.composemvi.presentation.ui.theme.ComposeMVITheme
import com.worldline.composemvi.presentation.utils.DevicePreviews
import com.worldline.composemvi.presentation.utils.LocalLoading
import com.worldline.composemvi.presentation.utils.UserNewsResourcePreviewParameterProvider
import com.worldline.composemvi.presentation.utils.launchCustomChromeTab
import com.worldline.composemvi.presentation.utils.rememberFlowWithLifecycle

@Composable
fun ForYouScreen(
    modifier: Modifier = Modifier,
    viewModel: ForYouViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val effect = rememberFlowWithLifecycle(viewModel.effect)
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

    LaunchedEffect(effect) {
        effect.collect { action ->
            when (action) {
                is ForYouEffect.NavigateToTopic -> {
                    // This effect would result in a navigation to another screen of the application
                    // with the topicId as a parameter.
                    Log.d("ForYouScreen", "Navigate to topic with id: ${action.topicId}")
                }

                is ForYouEffect.NavigateToNews -> launchCustomChromeTab(
                    context,
                    Uri.parse(action.newsUrl),
                    backgroundColor
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getData()
    }

    ForYouScreenContent(
        modifier = modifier,
        topicsLoading = state.value.topicsLoading,
        topics = state.value.topics,
        topicsVisible = state.value.topicsVisible,
        newsLoading = state.value.newsLoading,
        news = state.value.news,
        onTopicCheckedChanged = { topicId, isChecked ->
            viewModel.sendEvent(
                event = ForYouScreenReducer.ForYouEvent.UpdateTopicIsFollowed(
                    topicId = topicId,
                    isFollowed = isChecked,
                )
            )
        },
        onTopicClick = viewModel::onTopicClick,
        saveFollowedTopics = {
            viewModel.sendEvent(
                event = ForYouScreenReducer.ForYouEvent.UpdateTopicsVisible(
                    isVisible = false
                )
            )
        },
        onNewsResourcesCheckedChanged = { newsResourceId, isChecked ->
            viewModel.sendEvent(
                event = ForYouScreenReducer.ForYouEvent.UpdateNewsIsSaved(
                    newsId = newsResourceId,
                    isSaved = isChecked,
                )
            )
        },
        onNewsResourceViewed = { newsResourceId ->
            viewModel.sendEvent(
                event = ForYouScreenReducer.ForYouEvent.UpdateNewsIsViewed(
                    newsId = newsResourceId,
                    isViewed = true,
                )
            )
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForYouScreenContent(
    topicsLoading: Boolean,
    topics: List<FollowableTopic>,
    topicsVisible: Boolean,
    newsLoading: Boolean,
    news: List<UserNewsResource>,
    onTopicCheckedChanged: (String, Boolean) -> Unit,
    onTopicClick: (String) -> Unit,
    saveFollowedTopics: () -> Unit,
    onNewsResourcesCheckedChanged: (String, Boolean) -> Unit,
    onNewsResourceViewed: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    // This code should be called when the UI is ready for use and relates to Time To Full Display.
    ReportDrawnWhen { !topicsLoading && !newsLoading }

    val itemsAvailable = remember {
        derivedStateOf {
            val topicsSize = if (topicsLoading) {
                0
            } else {
                1
            }

            val newsSize = if (newsLoading) {
                0
            } else {
                news.size
            }

            topicsSize + newsSize
        }
    }

    val state = rememberLazyStaggeredGridState()
    val scrollbarState = state.scrollbarState(
        itemsAvailable = itemsAvailable.value,
    )

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 24.dp,
            modifier = Modifier
                .testTag("forYou:feed"),
            state = state,
        ) {
            if (topicsVisible) {
                item(span = StaggeredGridItemSpan.FullLine, contentType = "onboarding") {
                    CompositionLocalProvider(LocalLoading provides topicsLoading) {
                        Column(
                            modifier = Modifier.layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                                    ),
                                )
                                layout(placeable.width, placeable.height) {
                                    placeable.place(0, 0)
                                }
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.feature_foryou_onboarding_guidance_title),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp),
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                text = stringResource(R.string.feature_foryou_onboarding_guidance_subtitle),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, start = 24.dp, end = 24.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            TopicSelection(
                                topics = topics,
                                onTopicCheckedChanged = onTopicCheckedChanged,
                                modifier = Modifier
                                    .padding(bottom = 8.dp),
                            )
                            // Done button
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                NiaButton(
                                    onClick = saveFollowedTopics,
                                    enabled = topics.any { it.isFollowed },
                                    modifier = Modifier
                                        .padding(horizontal = 24.dp)
                                        .widthIn(364.dp)
                                        .fillMaxWidth(),
                                ) {
                                    Text(
                                        text = stringResource(R.string.feature_foryou_done),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            items(
                items = news,
                key = { it.id },
                contentType = { "newsFeedItem" },
            ) { userNewsResource ->
                CompositionLocalProvider(LocalLoading provides newsLoading) {
                    NewsResourceCardExpanded(
                        userNewsResource = userNewsResource,
                        isBookmarked = userNewsResource.isSaved,
                        onClick = {
                            onNewsResourceViewed(userNewsResource.id)
                        },
                        hasBeenViewed = userNewsResource.hasBeenViewed,
                        onToggleBookmark = {
                            onNewsResourcesCheckedChanged(
                                userNewsResource.id,
                                !userNewsResource.isSaved,
                            )
                        },
                        onTopicClick = onTopicClick,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .animateItemPlacement(),
                    )
                }
            }

            item(span = StaggeredGridItemSpan.FullLine, contentType = "bottomSpacing") {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Add space for the content to clear the "offline" snackbar.
                    Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
                }
            }
        }
        AnimatedVisibility(
            visible = newsLoading || topicsLoading,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
            ) + fadeOut(),
        ) {
            val loadingContentDescription = stringResource(id = R.string.feature_foryou_loading)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                NiaOverlayLoadingWheel(
                    modifier = Modifier
                        .align(Alignment.Center),
                    contentDesc = loadingContentDescription,
                )
            }
        }
        state.DraggableScrollbar(
            modifier = Modifier
                .fillMaxHeight()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 2.dp)
                .align(Alignment.CenterEnd),
            state = scrollbarState,
            orientation = Orientation.Vertical,
            onThumbMoved = state.rememberDraggableScroller(
                itemsAvailable = itemsAvailable.value,
            ),
        )
    }
    NotificationPermissionEffect()
}

@DevicePreviews
@Composable
fun ForYouScreenPopulatedFeed(
    @PreviewParameter(UserNewsResourcePreviewParameterProvider::class)
    userNewsResources: List<UserNewsResource>,
) {
    ComposeMVITheme {
        ForYouScreenContent(
            topicsLoading = false,
            newsLoading = false,
            topics = emptyList(),
            topicsVisible = false,
            news = userNewsResources,
            onTopicCheckedChanged = { _, _ -> },
            saveFollowedTopics = {},
            onNewsResourcesCheckedChanged = { _, _ -> },
            onNewsResourceViewed = {},
            onTopicClick = {},
        )
    }
}

@DevicePreviews
@Composable
fun ForYouScreenOfflinePopulatedFeed(
    @PreviewParameter(UserNewsResourcePreviewParameterProvider::class)
    userNewsResources: List<UserNewsResource>,
) {
    ComposeMVITheme {
        ForYouScreenContent(
            topicsLoading = false,
            newsLoading = false,
            topics = emptyList(),
            topicsVisible = false,
            news = userNewsResources,
            onTopicCheckedChanged = { _, _ -> },
            saveFollowedTopics = {},
            onNewsResourcesCheckedChanged = { _, _ -> },
            onNewsResourceViewed = {},
            onTopicClick = {},
        )
    }
}

@DevicePreviews
@Composable
fun ForYouScreenTopicSelection(
    @PreviewParameter(UserNewsResourcePreviewParameterProvider::class)
    userNewsResources: List<UserNewsResource>,
) {
    ComposeMVITheme {
        ForYouScreenContent(
            topicsLoading = false,
            newsLoading = false,
            topics = userNewsResources.flatMap { news -> news.followableTopics }
                .distinctBy { it.topic.id },
            topicsVisible = true,
            news = userNewsResources,
            onTopicCheckedChanged = { _, _ -> },
            saveFollowedTopics = {},
            onNewsResourcesCheckedChanged = { _, _ -> },
            onNewsResourceViewed = {},
            onTopicClick = {},
        )
    }
}

@DevicePreviews
@Composable
fun ForYouScreenLoading() {
    ComposeMVITheme {
        ForYouScreenContent(
            topicsLoading = true,
            newsLoading = true,
            topics = emptyList(),
            topicsVisible = false,
            news = emptyList(),
            onTopicCheckedChanged = { _, _ -> },
            saveFollowedTopics = {},
            onNewsResourcesCheckedChanged = { _, _ -> },
            onNewsResourceViewed = {},
            onTopicClick = {},
        )
    }
}

@DevicePreviews
@Composable
fun ForYouScreenPopulatedAndLoading(
    @PreviewParameter(UserNewsResourcePreviewParameterProvider::class)
    userNewsResources: List<UserNewsResource>,
) {
    ComposeMVITheme {
        ForYouScreenContent(
            topicsLoading = true,
            newsLoading = false,
            topics = emptyList(),
            topicsVisible = false,
            news = userNewsResources,
            onTopicCheckedChanged = { _, _ -> },
            saveFollowedTopics = {},
            onNewsResourcesCheckedChanged = { _, _ -> },
            onNewsResourceViewed = {},
            onTopicClick = {},
        )
    }
}