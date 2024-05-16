package com.worldline.composemvi.domain.model

import kotlin.random.Random

/**
 * A [topic] with the additional information for whether or not it is followed.
 */
// TODO consider changing to UserTopic and flattening
data class FollowableTopic(
    val topic: Topic,
    val isFollowed: Boolean,
) {
    companion object {
        fun fake(): FollowableTopic {
            return FollowableTopic(
                topic = Topic.fake(),
                isFollowed = Random.nextBoolean(),
            )
        }
    }
}
